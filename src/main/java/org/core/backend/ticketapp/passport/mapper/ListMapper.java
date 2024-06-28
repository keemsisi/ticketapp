package org.core.backend.ticketapp.passport.mapper;

import io.jsonwebtoken.lang.Collections;
import org.postgresql.jdbc.PgArray;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;


public class ListMapper implements RowMapper<List<String>> {

    @Override
    public List<String> mapRow(ResultSet rs, int arg1) throws SQLException {
        var actions = (List<String>)Collections.arrayToList(((PgArray)rs.getObject("user_actions")).getArray());
        var groupActions = (List<String>)Collections.arrayToList(((PgArray)rs.getObject("group_actions")).getArray());
        var list = new LinkedHashSet<String>();
        list.addAll(actions);
        list.addAll(groupActions);
        return list.stream().collect(Collectors.toList());
    }
}