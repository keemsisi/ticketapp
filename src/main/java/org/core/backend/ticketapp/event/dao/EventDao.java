package org.core.backend.ticketapp.event.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.common.response.EventStatsDTO;
import org.core.backend.ticketapp.common.response.EventStatsResponseDTO;
import org.core.backend.ticketapp.common.response.EventTicketStatsDTO;
import org.core.backend.ticketapp.common.response.EventTransactionDateStatsDTO;
import org.core.backend.ticketapp.event.dto.EventStatRequestDTO;
import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.core.backend.ticketapp.passport.mapper.EventWishedListDTO;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.*;

@Component
public class EventDao extends BaseDao {

    @Autowired
    DataSource dataSource;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private static @org.jetbrains.annotations.NotNull CallableStatementCreatorFactory
    getCallableStatementCreatorFactory(final String finalBaseQuery,
                                       final StringBuilder subQuery,
                                       final String paginationQuery) {
        var eventsQuery = String.format(finalBaseQuery, "e.*", subQuery) + paginationQuery;
        var countQuery = String.format(finalBaseQuery, "count(*) as count", subQuery);
        var eventWistListQuery = String.format(finalBaseQuery, "ew.event_id, ew.id", subQuery);
        eventsQuery = eventsQuery.replaceAll(":eventWishedListSubQuery", "");
        countQuery = countQuery.replaceAll(":eventWishedListSubQuery", "");
        eventWistListQuery = eventWistListQuery.replaceAll(":eventWishedListSubQuery",
                "RIGHT OUTER JOIN event_wishlist ew ON ew.event_id = e.id AND ew.user_id = u.id AND ew.deleted=false");
        final var finalQuery = ":eventsQuery;:countQuery;:eventWistListQuery;"
                .replaceAll(":eventsQuery", eventsQuery)
                .replaceAll(":countQuery", countQuery)
                .replaceAll(":eventWistListQuery", eventWistListQuery);
        return new CallableStatementCreatorFactory(finalQuery);
    }

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @SuppressWarnings("unchecked")
    public Page<EventResponseDTO> filterSearch(final EventFilterRequestDTO request) {
        final var skip = request.getPage() * request.getSize();
        final var limit = request.getSize();
        assert getJdbcTemplate() != null;
        var baseSQL = """
                SELECT DISTINCT %s FROM event e :innerQuery :seatSectionInnerQuery
                INNER JOIN users u ON u.id = e.user_id AND u.deleted=false
                :eventWishedListSubQuery WHERE e.deleted=false %s
                """;
        final var seatSectionInnerQuery = new StringBuilder(" INNER JOIN event_seat_sections ss ON ss.event_id = e.id ");
        final var innerQuery = new StringBuilder();
        final var order = ObjectUtils.defaultIfNull(request.getOrder(), Sort.Direction.DESC);
        final var paginationQuery = String.format(" ORDER BY e.date_created %s OFFSET %s LIMIT %s ", order.name(), skip, limit);
        final var subQuery = new StringBuilder();
        if (Objects.nonNull(request.getIsPaidEvent())) {
            subQuery.append(String.format(" AND e.free_event = '%s' ", request.getIsPaidEvent()));
        }

        //All inner join query should be here, continue it with if(...){}
        if (Objects.nonNull(request.getUserType()) && request.getUserType().isBuyer() && request.getIsBuyerEvent()) {
            innerQuery.append(String.format("""
                    INNER JOIN ticket tk ON tk.event_id = e.id
                    AND tk.deleted=false AND tk.user_id = '%s' 
                    """, request.getUserId()));
            seatSectionInnerQuery.append(" AND tk.seat_section = ss.id ");
        }
        baseSQL = baseSQL.replace(":innerQuery", innerQuery);

        if (Objects.nonNull(request.getLocation())) {
            subQuery.append(" AND e.location iLIKE '%").append(request.getLocation()).append("%'");
        }
        if (StringUtils.isNotBlank(request.getAddress())) {
            subQuery.append(" AND e.street_address iLIKE '%").append(request.getAddress()).append("%'");
        }
        if (Objects.nonNull(request.getEventCategory())) {
            subQuery.append(" AND e.categories::varchar iLIKE '%").append(request.getEventCategory()).append("%'");
        }
        if (StringUtils.isNotBlank(request.getArtistName())) {
            subQuery.append(" AND e.description iLIKE '%").append(request.getArtistName()).append("%'");
        }

        if (Objects.nonNull(request.getApprovalStatus())) {
            subQuery.append(String.format(" AND e.approval_status = '%s' ", request.getApprovalStatus()));
        }
        if (Objects.nonNull(request.getDescription())) {
            subQuery.append(" AND e.description iLIKE '%").append(request.getDescription()).append("%'");
        }
        if (Objects.nonNull(request.getIsPhysicalEvent())) {
            subQuery.append(String.format(" AND e.physical_event = '%s' ", request.getIsPhysicalEvent()));
        }
        if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
            subQuery.append(String.format(" AND e.event_date BETWEEN '%s' AND '%s' ", request.getStartDate(), request.getEndDate()));
        } else if (Objects.nonNull(request.getStartDate())) {
            subQuery.append(String.format(" AND e.event_date BETWEEN '%s' AND now() ", request.getStartDate()));
        }
        if (Objects.nonNull(request.getStartPrice()) && Objects.nonNull(request.getEndPrice())) {
            subQuery.append(String.format(" AND ss.price BETWEEN '%s' AND '%s' ", request.getStartPrice(), request.getEndPrice()));
        } else if (Objects.nonNull(request.getStartPrice())) {
            subQuery.append(String.format(" AND ss.price >= '%s' ", request.getStartPrice()));
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            subQuery.append(String.format(" AND e.title = '%s' ", request.getTitle()));
        }
        if (Objects.nonNull(request.getSeatSectionId())) {
            subQuery.append(String.format(" AND ss.id = '%s' ", request.getSeatSectionId()));
        }
        if (Objects.nonNull(request.getSeatSectionType())) {
            subQuery.append(String.format(" AND ss.type = '%s' ", request.getSeatSectionType()));
        }
        if (Objects.nonNull(request.getEventTicketType())) {
            subQuery.append(String.format(" AND e.ticket_type = '%s' ", request.getEventTicketType()));
        }
        if (Objects.nonNull(request.getSearch())) {
            subQuery.append((" AND " +
                    "( e.title             iLIKE '%:search%' " +
                    "  OR e.description    iLIKE '%:search%' " +
                    "  OR e.location       iLIKE '%:search%' " +
                    "  OR e.street_address iLIKE '%:search%' " +
                    ") "
            ).replaceAll(":search", request.getSearch()));
        }
        subQuery.append(getUserSubQuery(request));
        subQuery.append(determineTenantQuery(request.getTenantId()));
        var finalBaseQuery = subQuery.toString().contains("ss.") ? baseSQL.replace(":seatSectionInnerQuery", seatSectionInnerQuery) :
                baseSQL.replace(":seatSectionInnerQuery", "");
        finalBaseQuery = finalBaseQuery.replace(":innerQuery", "");
        final var cscFactory = getCallableStatementCreatorFactory(finalBaseQuery, subQuery, paginationQuery);
        final var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("events", new EventResponseRowMapper(objectMapper)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)),
                new SqlReturnResultSet("eventWishedList", BeanPropertyRowMapper.newInstance(EventWishedListDTO.class)));
        final var csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        final var results = getJdbcTemplate().call(csc, returnedParams);
        final var pagedResults = new Page<EventResponseDTO>();
        final var events = (List<EventResponseDTO>) results.get("events");
        final var counts = ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount();
        final var eventWishedList = ((ArrayList<EventWishedListDTO>) results.get("eventWishedList"));
        if (ObjectUtils.isNotEmpty(eventWishedList)) {
            events.forEach(event -> eventWishedList.forEach(eventWished -> {
                if (eventWished.getEventId().equals(event.getId())) {
                    event.setWishedList(true);
                }
            }));
        }
        pagedResults.setContent(events);
        pagedResults.setCount(counts);
        pagedResults.setSize(events.size());
        pagedResults.setPageNumber(request.getPage());
        pagedResults.setReqSize(request.getSize());
        pagedResults.setLast(events.isEmpty());
        pagedResults.setNumberOfElements(events.size());
        pagedResults.setTotalElements(counts);
        return pagedResults;
    }


    @SuppressWarnings("unchecked")
    public EventTicketStatsDTO getEventTicketStats(final UUID seatSectionId) {
        final var baseSQL = "SELECT " +
                " (SELECT ess2.capacity FROM event_seat_sections ess2 WHERE ess2.id = ':seatSectionId') AS total_capacity, " +
                " COUNT(tk) AS total_acquired_tickets,  " +
                " ((SELECT ess2.capacity FROM event_seat_sections ess2 WHERE ess2.id = ':seatSectionId') - COUNT(tk)) AS total_available_tickets " +
                " FROM ticket tk INNER JOIN event_seat_sections ess ON ess.id = ':seatSectionId' WHERE ess.deleted = false;";
        final var results = executeQuery(baseSQL.replace(":seatSectionId",
                seatSectionId.toString()), EventTicketStatsDTO.class);
        final var result = ((List<EventTicketStatsDTO>) results.get("result"));
        if (ObjectUtils.isNotEmpty(result)) return ((List<EventTicketStatsDTO>) results.get("result")).get(0);
        return EventTicketStatsDTO.builder().build();
    }

    @SuppressWarnings("unchecked")
    public EventStatsResponseDTO getEventsStats(final EventStatRequestDTO request) {
        final var orderBaseSQL = new StringBuilder("SELECT " +
                " SUM(CASE WHEN o.type = 'EVENT_TICKET' AND o.event_id = e.id THEN 1 ELSE 0 END)                             total_orders, " +
                " SUM(CASE WHEN o.type = 'EVENT_TICKET' AND o.event_id = e.id AND o.status = 'PENDING' THEN 1 ELSE 0 END)    total_pending, " +
                " SUM(CASE WHEN o.type = 'EVENT_TICKET' AND o.event_id = e.id AND o.status = 'COMPLETED' THEN 1 ELSE 0 END)  total_successful, " +
                " SUM(CASE WHEN o.type = 'EVENT_TICKET' AND o.event_id = e.id AND o.status = 'CANCELLED' THEN 1 ELSE 0 END)  total_cancelled, " +
                " SUM(CASE WHEN o.type = 'EVENT_TICKET' AND o.event_id = e.id AND o.status = 'FAILED' THEN 1 ELSE 0 END)     total_failed " +
                " FROM event e INNER  JOIN orders o ON o.event_id = e.id AND o.deleted = false WHERE  e.deleted = false ");
        final var transactionBaseSQL = new StringBuilder("SELECT " +
                " SUM(CASE   WHEN t.event_id = e.id AND t.type   = 'EVENT_SETTLEMENT' AND t.event_id = e.id THEN 1 ELSE 0 END) total_settlements, " +
                " SUM(CASE   WHEN t.event_id = e.id AND t.status = 'PENDING' THEN 1 ELSE 0 END)                                total_pending, " +
                " SUM(CASE   WHEN t.event_id = e.id AND t.status = 'COMPLETED' THEN 1 ELSE 0 END)                              total_successful, " +
                " SUM(CASE   WHEN t.event_id = e.id AND t.status = 'CANCELLED' THEN 1 ELSE 0 END)                              total_cancelled, " +
                " SUM(CASE   WHEN t.event_id = e.id AND t.status = 'FAILED' THEN 1 ELSE 0 END)                                 total_failed, " +
                " SUM(CASE   WHEN t.type = 'EVENT_TICKET' THEN t.amount ELSE 0 END)                                            total_sales_amount, " +
                " SUM(CASE   WHEN t.type = 'EVENT_SETTLEMENT' AND t.order_id = e.id AND t.status = 'COMPLETED' THEN t.amount ELSE 0 END) total_settled_amount " +
                " FROM event e INNER  JOIN transaction t ON t.event_id = e.id AND t.deleted = false WHERE  e.deleted = false ");
        final var transactionDateStats = new StringBuilder("SELECT " +
                " EXTRACT(YEAR FROM t.date_created) AS year, " +
                " EXTRACT(MONTH FROM t.date_created) AS month, " +
                " t.status, SUM(t.amount) AS total_amount  " +
                " FROM event e INNER  JOIN transaction t ON t.event_id = e.id AND t.deleted = false WHERE  e.deleted = false ");

        if (Objects.nonNull(request.getEventId())) {
            orderBaseSQL.append(String.format(" AND e.id = '%s' ", request.getEventId()));
            transactionBaseSQL.append(String.format(" AND e.id = '%s' ", request.getEventId()));
            transactionDateStats.append(String.format(" AND e.id = '%s' ", request.getEventId()));
        }

        if (Objects.nonNull(request.getEventId())) {
            orderBaseSQL.append(String.format(" AND e.id = '%s' ", request.getEventId()));
            transactionBaseSQL.append(String.format(" AND e.id = '%s' ", request.getEventId()));
            transactionDateStats.append(String.format(" AND e.id = '%s' ", request.getEventId()));
        }
        if (Objects.nonNull(request.getUserId())) {
            orderBaseSQL.append(String.format(" AND o.user_id = '%s' ", request.getUserId()));
            transactionBaseSQL.append(String.format(" AND t.user_id = '%s' ", request.getUserId()));
            transactionDateStats.append(String.format(" AND t.user_id = '%s' ", request.getUserId()));
        }
        if (Objects.nonNull(request.getTenantId())) {
            orderBaseSQL.append(String.format(" AND e.tenant_id = '%s' ", request.getTenantId()));
            transactionBaseSQL.append(String.format(" AND e.tenant_id = '%s' ", request.getTenantId()));
            transactionDateStats.append(String.format(" AND e.tenant_id = '%s' ", request.getTenantId()));
        }
        if (Objects.nonNull(request.getStartDate()) && Objects.isNull(request.getEndDate())) {
            orderBaseSQL.append(String.format(" AND e.date_created >= '%s' ", request.getStartDate()));
            transactionBaseSQL.append(String.format(" AND e.date_created >= '%s' ", request.getStartDate()));
            transactionDateStats.append(String.format(" AND e.date_created >= '%s' ", request.getStartDate()));
        }
        if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
            orderBaseSQL.append(String.format(" AND e.date_created BETWEEN '%s' AND '%s' ", request.getStartDate(), request.getEndDate()));
            transactionBaseSQL.append(String.format(" AND e.date_created BETWEEN '%s' AND '%s' ", request.getStartDate(), request.getEndDate()));
            transactionDateStats.append(String.format(" AND e.date_created BETWEEN '%s' AND '%s' ", request.getStartDate(), request.getEndDate()));
        }
        transactionDateStats.append(" GROUP BY year, month, t.status ORDER BY year, month, t.status ");
        final var finalQuery = String.format("%s;%s;%s", orderBaseSQL, transactionBaseSQL, transactionDateStats);
        final var cscFactory = new CallableStatementCreatorFactory(finalQuery);
        final var returnedParams = List.<SqlParameter>of(
                new SqlReturnResultSet("order_stats", BeanPropertyRowMapper.newInstance(EventStatsDTO.class)),
                new SqlReturnResultSet("transaction_stats", BeanPropertyRowMapper.newInstance(EventStatsDTO.class)),
                new SqlReturnResultSet("transaction_date_stats", BeanPropertyRowMapper.newInstance(EventTransactionDateStatsDTO.class)));
        final var csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        assert getJdbcTemplate() != null;
        final @NotNull Map<String, Object> results = jdbcTemplate.call(csc, returnedParams);
        final var orderResult = ((List<EventStatsDTO>) results.get("order_stats"));
        final var orderData = !orderResult.isEmpty() ? ((List<EventStatsDTO>) results.get("order_stats")).get(0) : new EventStatsDTO();
        final var transactionResult = ((List<EventStatsDTO>) results.get("transaction_stats"));
        final var transactionData = !transactionResult.isEmpty() ? ((List<EventStatsDTO>) results.get("transaction_stats")).get(0) : new EventStatsDTO();
        final var transactionDateResult = ((List<EventTransactionDateStatsDTO>) results.get("transaction_date_stats"));
        final var transactionDateData = !transactionDateResult.isEmpty() ?
                ((List<EventTransactionDateStatsDTO>) results.get("transaction_date_stats")) :
                new ArrayList<EventTransactionDateStatsDTO>();
        return EventStatsResponseDTO.builder().orderStats(orderData).transactionStats(transactionData)
                .transactionDateStats(transactionDateData)
                .build();
    }

    private UUID getUserIdIfNotAdmin() {
        final var user = jwtTokenUtil.getUser();
        return user.getRoles().contains("tenant_owner") ? null : user.getUserId();
    }

    private String getUserSubQuery(final EventFilterRequestDTO request) {
        if (isBuyerEvent(request)) {
            return " AND e.user_id = '%s' ";
        }
        return " AND e.user_id IS NOT NULL ";
    }

    private boolean isBuyerEvent(final EventFilterRequestDTO request) {
        return !request.getIsBuyerEvent() && Objects.nonNull(request.getUserId());
    }

    private boolean hasUserId(final EventFilterRequestDTO request) {
        return Objects.nonNull(request.getUserId());
    }


    private boolean isTenantOwnerOrUser() {
        final var user = jwtTokenUtil.getUser();
        return user.getRoles().stream().anyMatch(s ->
                List.of("tenant_owner", "tenant_user").contains(s));
    }

    private String determineTenantQuery(final UUID reqTenantId) {
        final var user = jwtTokenUtil.getUser();
        final var userRoles = user.getRoles();
        final var tenantId = user.getTenantId();
        final var tenantQuery = new StringBuilder();
        if (Objects.nonNull(user.getUserId()) && Objects.isNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(AccountType.INDIVIDUAL.name()))) {
            tenantQuery.append(" AND e.tenant_id IS NOT NULL AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ");
        } else if (Objects.nonNull(user.getUserId()) && Objects.nonNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(AccountType.INDIVIDUAL.name()))) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ", reqTenantId));
        } else if (Objects.isNull(user.getUserId()) && Objects.nonNull(reqTenantId)) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ", reqTenantId));
        } else if (Objects.isNull(user.getUserId()) && Objects.isNull(reqTenantId)) {
//            tenantQuery.append(" AND e.tenant_id IS NOT NULL AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ");
            tenantQuery.append(" AND e.tenant_id IS NOT NULL ");
        } else if (UserUtils.userHasAnyRole(userRoles, AccountType.getPossibleAminAccountType())) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' ", tenantId));
        } else if (Objects.nonNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(AccountType.SUPER_ADMIN.name(), AccountType.SYSTEM_ADMIN_USER.name()))) {
            tenantQuery.append(" AND e.tenant_id = '%s' ").append(reqTenantId);
        } else if (Objects.isNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(AccountType.SUPER_ADMIN.name(), AccountType.SYSTEM_ADMIN_USER.name()))) {
            tenantQuery.append(" AND e.tenant_id IS NOT NULL ");
        }
        return tenantQuery.toString();
    }

    private <E> Map<String, Object> executeQuery(final String query, final Class<E> _class) {
        final var cscFactory = new CallableStatementCreatorFactory(query);
        final var returnedParams = List.<SqlParameter>of(
                new SqlReturnResultSet("result", BeanPropertyRowMapper.newInstance(_class)));
        final var csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        assert getJdbcTemplate() != null;
        final @NotNull Map<String, Object> results = jdbcTemplate.call(csc, returnedParams);
        return results;
    }


}
