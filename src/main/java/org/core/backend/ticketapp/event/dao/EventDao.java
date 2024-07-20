package org.core.backend.ticketapp.event.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.enums.UserType;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ResponsePageRequest;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

@Component
public class EventDao extends BaseDao {

    @Autowired
    DataSource dataSource;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @SuppressWarnings("unchecked")
    public Page<EventResponseDTO> filterSearch(EventFilterRequestDTO filterRequest) {
        assert getJdbcTemplate() != null;
        final var baseSQL = " SELECT %s FROM event e :innerQuery " +
                " INNER JOIN users u ON u.id = e.user_id AND u.deleted=false WHERE e.deleted=false %s ";
        final var seatSectionInnerQuery = " INNER JOIN event_seat_sections ss ON ss.event_id = e.id ";
        final var order = ObjectUtils.defaultIfNull(filterRequest.getOrder(), Sort.Direction.DESC);
        final var subQuery = new StringBuilder();
        if (Objects.nonNull(filterRequest.getIsPaidEvent())) {
            subQuery.append(String.format(" AND e.free_event = '%s' ", filterRequest.getIsPaidEvent()));
        }
        if (Objects.nonNull(filterRequest.getLocation())) {
            subQuery.append(" AND e.location iLIKE '%").append(filterRequest.getLocation()).append("%'");
        }
        if (StringUtils.isNotBlank(filterRequest.getAddress())) {
            subQuery.append(" AND e.street_address iLIKE '%").append(filterRequest.getAddress()).append("%'");
        }
        if (Objects.nonNull(filterRequest.getEventCategory())) {
            subQuery.append(" AND e.categories::varchar iLIKE '%").append(filterRequest.getEventCategory()).append("%'");
        }
        if (StringUtils.isNotBlank(filterRequest.getArtistName())) {
            subQuery.append(" AND e.description iLIKE '%").append(filterRequest.getArtistName()).append("%'");
        }

        if (Objects.nonNull(filterRequest.getApprovalStatus())) {
            subQuery.append(String.format(" AND e.approval_status = '%s' ", filterRequest.getApprovalStatus()));
        }
        if (Objects.nonNull(filterRequest.getDescription())) {
            subQuery.append(" AND e.description iLIKE '%").append(filterRequest.getDescription()).append("%'");
        }
        if (Objects.nonNull(filterRequest.getIsPhysicalEvent())) {
            subQuery.append(String.format(" AND e.description = '%s' ", filterRequest.getIsPhysicalEvent()));
        }
        if (Objects.nonNull(filterRequest.getStartDate()) && Objects.nonNull(filterRequest.getEndDate())) {
            subQuery.append(String.format(" AND e.event_date BETWEEN '%s' AND '%s' ", filterRequest.getStartDate(), filterRequest.getEndDate()));
        } else if (Objects.nonNull(filterRequest.getStartDate())) {
            subQuery.append(String.format(" AND e.event_date BETWEEN '%s' AND now() ", filterRequest.getStartDate()));
        }
        if (Objects.nonNull(filterRequest.getStartPrice()) && Objects.nonNull(filterRequest.getEndPrice())) {
            subQuery.append(String.format(" AND ss.price BETWEEN '%s' AND '%s' ", filterRequest.getStartPrice(), filterRequest.getEndPrice()));
        } else if (Objects.nonNull(filterRequest.getStartPrice())) {
            subQuery.append(String.format(" AND ss.price >= '%s' ", filterRequest.getStartPrice()));
        }
        if (StringUtils.isNotBlank(filterRequest.getTitle())) {
            subQuery.append(String.format(" AND e.title = '%s' ", filterRequest.getTitle()));
        }
        if (Objects.nonNull(filterRequest.getSeatSectionId())) {
            subQuery.append(String.format(" AND ss.id = '%s' ", filterRequest.getSeatSectionId()));
        }
        if (Objects.nonNull(filterRequest.getSeatSectionType())) {
            subQuery.append(String.format(" AND ss.type = '%s' ", filterRequest.getSeatSectionType()));
        }
        if (Objects.nonNull(filterRequest.getEventTicketType())) {
            subQuery.append(String.format(" AND e.ticket_type = '%s' ", filterRequest.getEventTicketType()));
        }
        subQuery.append(getUserSubQuery(filterRequest.getUserId()));
        subQuery.append(determineTenantQuery(filterRequest.getTenantId()));
        var finalBaseQuery = subQuery.toString().contains("ss.") ? baseSQL.replace(":innerQuery", seatSectionInnerQuery) :
                baseSQL.replace(":innerQuery", "");
        final var pageable = ResponsePageRequest.createPageRequest(filterRequest.getPage(),
                filterRequest.getSize(), order, new String[]{"dateCreated"}, true, "dateCreated");
        final var eventsQuery = String.format(finalBaseQuery, "e.*", subQuery);
        final var countQuery = String.format(finalBaseQuery, "count(*) as count", subQuery);
        final var finalQuery = ":eventsQuery;:countQuery;"
                .replaceAll(":eventsQuery", eventsQuery)
                .replaceAll(":countQuery", countQuery);
        var cscFactory = new CallableStatementCreatorFactory(finalQuery);
        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("events", BeanPropertyRowMapper.newInstance(EventResponseDTO.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        Map<String, Object> results = getJdbcTemplate().call(csc, returnedParams);
        return PageableExecutionUtils.getPage((List<EventResponseDTO>) results.get("events"), pageable,
                () -> ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount());
    }

    private UUID getUserIdIfNotAdmin() {
        final var user = jwtTokenUtil.getUser();
        return user.getRoles().contains("tenant_owner") ? null : user.getUserId();
    }

    private String getUserSubQuery(final UUID userId) {
        if (Objects.nonNull(userId)) {
            return " AND e.user_id = '%s' ";
        }
        return " AND e.user_id IS NOT NULL ";
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
        if (Objects.nonNull(user.getUserId()) && Objects.isNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(UserType.INDIVIDUAL.name()))) {
            tenantQuery.append(" AND e.tenant_id IS NOT NULL AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ");
        } else if (Objects.nonNull(user.getUserId()) && Objects.nonNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(UserType.INDIVIDUAL.name()))) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ", reqTenantId));
        } else if (Objects.isNull(user.getUserId()) && Objects.nonNull(reqTenantId)) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ", reqTenantId));
        } else if (Objects.isNull(user.getUserId()) && Objects.isNull(reqTenantId)) {
//            tenantQuery.append(" AND e.tenant_id IS NOT NULL AND u.type NOT IN ('SUPER_ADMIN','SUPER_ADMIN_USER') ");
            tenantQuery.append(" AND e.tenant_id IS NOT NULL ");
        } else if (UserUtils.userHasAnyRole(userRoles, List.of(UserType.TENANT_ADMIN.name(), UserType.TENANT_USER.name()))) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' ", tenantId));
        } else if (Objects.nonNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(UserType.SUPER_ADMIN.name(), UserType.SYSTEM_ADMIN_USER.name()))) {
            tenantQuery.append(" AND e.tenant_id = '%s' ").append(reqTenantId);
        } else if (Objects.isNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(UserType.SUPER_ADMIN.name(), UserType.SYSTEM_ADMIN_USER.name()))) {
            tenantQuery.append(" AND e.tenant_id IS NOT NULL ");
        } else if (Objects.nonNull(reqTenantId) && UserUtils.userHasAnyRole(userRoles, List.of(UserType.SUPER_ADMIN.name(), UserType.SYSTEM_ADMIN_USER.name()))) {
            tenantQuery.append(String.format(" AND e.tenant_id = '%s' ", reqTenantId));
        }
        return tenantQuery.toString();
    }

}
