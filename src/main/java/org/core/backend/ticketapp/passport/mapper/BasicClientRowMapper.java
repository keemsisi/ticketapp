package org.core.backend.ticketapp.passport.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.q2o.Q2Obj;
import io.jsonwebtoken.lang.Collections;
import org.core.backend.ticketapp.passport.dtos.core.BasicClientDetails;
import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BasicClientRowMapper implements RowMapper<BasicClientDetails> {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public BasicClientDetails mapRow(ResultSet resultSet, int i) throws SQLException {
        var client = Q2Obj.fromResultSet(resultSet, new BasicClientDetails());
        client.setDomains(Collections.arrayToList(((PgArray) resultSet.getObject("domains")).getArray()));
        return client;
    }
}