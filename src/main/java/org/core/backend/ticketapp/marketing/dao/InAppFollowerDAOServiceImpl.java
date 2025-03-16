package org.core.backend.ticketapp.marketing.dao;

import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InAppFollowerDAOServiceImpl extends BaseDao implements InAppFollowerDAOService {

    @Autowired
    DataSource dataSource;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static String getFollowBaseQuery(FilterInAppFollowerRequestDTO request) {
        var baseSQL = """
                FROM in_app_follower iaf
                INNER JOIN users u ON u.id = iaf.followed_user_id AND u.deleted=false
                WHERE iaf.:column = ':userId' AND iaf.deleted = false
                """.replaceAll(":userId", request.getUserId().toString());

        if (request.getType() == FilterInAppFollowerRequestDTO.FollowType.FOLLOWERS) {
            baseSQL = baseSQL.replace(":column", "followed_user_id");
        }

        if (request.getType() == FilterInAppFollowerRequestDTO.FollowType.FOLLOWING) {
            baseSQL = baseSQL.replace(":column", "user_id");
        }
        return baseSQL;
    }

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Page<InAppFollower> filterSearchFollowers(FilterInAppFollowerRequestDTO request, Pageable pageable) {
        final var skip = pageable.getPageNumber() * pageable.getPageSize();
        final var limit = pageable.getPageSize();
        assert getJdbcTemplate() != null;
        var baseSQL = getFollowBaseQuery(request);
        var selectQuery = """
                SELECT iaf.*, u.first_name, u.last_name, u.middle_name
                :baseQuery
                ORDER BY e.date_created DESC OFFSET :offSet LIMIT :limit;
                """
                .replaceAll(":baseQuery", baseSQL)
                .replaceAll(":limit", String.valueOf(limit))
                .replaceAll(":offSet", String.valueOf(skip));
        var countQuery = """
                SELECT COUNT(*)
                :baseQuery;
                """.replaceAll(":baseQuery", baseSQL);
        final var response = executeQueryWithCount(selectQuery + countQuery, InAppFollower.class);
        final var results = (List<InAppFollower>) response.get("result");
        final var count = response.get("count");

        final var pagedResults = new Page<InAppFollower>();
        pagedResults.setContent(results);
        pagedResults.setCount((long) results.size());
        pagedResults.setSize(limit);
        pagedResults.setPageNumber(pageable.getPageNumber());
        pagedResults.setReqSize(pageable.getPageSize());
        pagedResults.setLast(results.isEmpty());
        pagedResults.setNumberOfElements(results.size());
        pagedResults.setTotalElements((Long) count);
        return pagedResults;
    }

    private <E> Map<String, Object> executeQueryWithCount(final String query, final Class<E> _class) {
        final var cscFactory = new CallableStatementCreatorFactory(query);
        final var returnedParams = List.<SqlParameter>of(
                new SqlReturnResultSet("result", BeanPropertyRowMapper.newInstance(_class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class))
        );
        final var csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        assert getJdbcTemplate() != null;
        final @NotNull Map<String, Object> results = jdbcTemplate.call(csc, returnedParams);
        return results;
    }

}
