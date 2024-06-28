package org.core.backend.ticketapp.passport.dao;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.entity.Action;
import org.core.backend.ticketapp.passport.entity.UserAction;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Component
public class ActionDao extends BaseDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public List<Action> getActionsByIds(List<UUID> actionIds) {
        String inSql = String.join(",", Collections.nCopies(actionIds.size(), "?"));
        var actions = getJdbcTemplate().query(String.format("SELECT * FROM action up WHERE id IN (%s)", inSql),
                actionIds.toArray(), BeanPropertyRowMapper.newInstance(Action.class));
        return actions;
    }

    public Optional<Set<UserAction>> getUsersByActionId(UUID actionId) {
        String sql = "SELECT up.*, u.first_name, u.last_name, u.middle_name, p.code FROM user_action up " +
                " LEFT JOIN action p ON p.id = up.action_id INNER JOIN users as u ON u.id = up.user_id " +
                " WHERE up.action_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserAction.class);
        var users = getJdbcTemplate().query(sql, rowMapper, actionId);
        return Optional.of(new HashSet<>(users));
    }

    public Page<Action> getAll(Pageable pageable) {
        int limit = pageable.getPageSize();
        Long offSet = pageable.getOffset();
        var cscFactory = new CallableStatementCreatorFactory(String.format("SELECT * FROM action n\n LIMIT %s OFFSET %s;", limit, offSet) + "SELECT COUNT(*) FROM action;");
        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("actions", BeanPropertyRowMapper.newInstance(Action.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        Map<String, Object> results = getJdbcTemplate().call(csc, returnedParams);
        return PageableExecutionUtils.getPage((List<Action>) results.get("actions"), pageable, () -> ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount());
    }
}
