package org.core.backend.ticketapp.event.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.request.events.EventFilterRequestDTO;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
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
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @SuppressWarnings("unchecked")
    public Page<Event> filterSearch(EventFilterRequestDTO filterRequest) {
        assert getJdbcTemplate() != null;
        final var baseSQL = " SELECT %s FROM event e " +
                " INNER JOIN event_seat_sections ss ON ss.event_id = e.id " +
                " WHERE 1=1 %s ";
        final var order = ObjectUtils.defaultIfNull(filterRequest.getOrder(), Sort.Direction.DESC);
        final var subQuery = new StringBuilder();
        if (Objects.nonNull(filterRequest.getIsPaidEvent())) {
            subQuery.append(" AND e.free_event = '%s' ").append(filterRequest.getIsPaidEvent());
        }
        if (Objects.nonNull(filterRequest.getLocation())) {
            subQuery.append(" AND e.location iLIKE '%").append(filterRequest.getLocation()).append("%'");
        }
        if (StringUtils.isNotBlank(filterRequest.getAddress())) {
            subQuery.append(" AND e.street_address iLIKE '%").append(filterRequest.getAddress()).append("%'");
        }
        if (Objects.nonNull(filterRequest.getEventCategory())) {
            subQuery.append(" AND e.category = '%s' ").append(filterRequest.getEventCategory());
        }
        if (StringUtils.isNotBlank(filterRequest.getArtistName())) {
            subQuery.append(" AND e.description iLIKE '%").append(filterRequest.getArtistName()).append("%'");
        }
        if (Objects.nonNull(filterRequest.getApprovalStatus())) {
            subQuery.append(" AND e.description iLIKE '%").append(filterRequest.getArtistName()).append("%'");
        }
        if (Objects.nonNull(filterRequest.getDescription())) {
            subQuery.append(" AND e.description iLIKE '%").append(filterRequest.getDescription()).append("%'");
        }
        if (Objects.nonNull(filterRequest.getIsPhysicalEvent())) {
            subQuery.append(" AND e.description = '%s' ").append(filterRequest.getIsPhysicalEvent());
        }
        if (Objects.nonNull(filterRequest.getStartDate()) && Objects.nonNull(filterRequest.getEndDate())) {
            subQuery.append(" AND e.event_date BETWEEN '%s' AND '%s' ").append(filterRequest.getStartDate()).append(filterRequest.getEndDate());
        } else if (Objects.nonNull(filterRequest.getStartDate())) {
            subQuery.append(" AND e.event_date BETWEEN '%s' AND now() ").append(filterRequest.getStartDate());
        }
        if (Objects.nonNull(filterRequest.getStartPrice()) && Objects.nonNull(filterRequest.getEndPrice())) {
            subQuery.append(" AND ss.price BETWEEN '%s' AND '%s' ").append(filterRequest.getStartPrice()).append(filterRequest.getEndPrice());
        } else if (Objects.nonNull(filterRequest.getStartPrice())) {
            subQuery.append(" AND ss.price >= '%s' ").append(filterRequest.getStartPrice());
        }
        if (StringUtils.isNotBlank(filterRequest.getTitle())) {
            subQuery.append(" AND e.title = '%s' ").append(filterRequest.getTitle());
        }
        if (Objects.nonNull(filterRequest.getSeatSectionId())) {
            subQuery.append(" AND ss.id = '%s' ").append(filterRequest.getSeatSectionId());
        }
        if (Objects.nonNull(filterRequest.getSeatSectionType())) {
            subQuery.append(" AND ss.type = '%s' ").append(filterRequest.getSeatSectionType());
        }
        final var pageable = ResponsePageRequest.createPageRequest(filterRequest.getPage(),
                filterRequest.getSize(), order, new String[]{"dateCreated"}, true, "dateCreated");
        final var eventsQuery = String.format(baseSQL, "e.*", subQuery);
        final var countQuery = String.format(baseSQL, "count(*) as count", subQuery);
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
}
