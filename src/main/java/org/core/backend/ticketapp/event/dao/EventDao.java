package org.core.backend.ticketapp.event.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ResponsePageRequest;
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
    public Page<Event> filterSearch(EventFilterRequestDTO filterRequest) {
        assert getJdbcTemplate() != null;
        final var baseSQL = " SELECT %s FROM event e :innerQuery WHERE e.deleted=false %s ";
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
            subQuery.append(String.format(" AND e.category = '%s' ", filterRequest.getEventCategory()));
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
        final var userId = ObjectUtils.defaultIfNull(filterRequest.getUserId(), getUserIdIfNotAdmin());
        subQuery.append(String.format(" AND e.user_id = '%s' ", userId));
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
                new SqlReturnResultSet("events", BeanPropertyRowMapper.newInstance(Event.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        Map<String, Object> results = getJdbcTemplate().call(csc, returnedParams);
        return PageableExecutionUtils.getPage((List<Event>) results.get("events"), pageable,
                () -> ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount());
    }

    private UUID getUserIdIfNotAdmin() {
        final var user = jwtTokenUtil.getUser();
        return user.getRoles().contains("system_admin") ? null : user.getUserId();
    }
}
