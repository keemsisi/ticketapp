package org.core.backend.ticketapp.passport.mapper;

import com.zaxxer.q2o.Q2Obj;
import io.jsonwebtoken.lang.Collections;
import org.core.backend.ticketapp.passport.entity.User;
import org.postgresql.jdbc.PgArray;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int arg1) throws SQLException {

        var user = Q2Obj.fromResultSet(rs, new User());
        var userActions = Collections.arrayToList(((PgArray) rs.getObject("user_actions")).getArray());
        user.setRoles(Collections.arrayToList(((PgArray) rs.getObject("roles")).getArray()));
        user.setGroups(Collections.arrayToList(((PgArray) rs.getObject("user_groups")).getArray()));
        var usergroupActions = Collections.arrayToList(((PgArray) rs.getObject("group_actions")).getArray());
        var actions = new LinkedHashSet<String>();
        actions.addAll(userActions);
        actions.addAll(usergroupActions);
        user.setActions(actions.stream().collect(Collectors.toList()));
        return user;
    }
}