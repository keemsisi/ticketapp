package org.core.backend.ticketapp.passport.dao;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class BaseDao<T> extends JdbcDaoSupport {
    final String SINGLE_RESULT = "object";
    final String MULTIPLE_RESULT = "list";
    final String RESULT_COUNT = "count";
    SimpleJdbcCall create, get;

    public T get(Long id) {
        SqlParameterSource in = new MapSqlParameterSource("id", id);
        return withSingleResultSet(in, get);
    }

    T withSingleResultSet(SqlParameterSource in, SimpleJdbcCall jdbcCall) {
        Map<String, Object> m = jdbcCall.execute(in);
        List<T> list = getJdbcResult((m), SINGLE_RESULT);
        if (Objects.nonNull(list) && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    List<T> withMultipleResultSet(SqlParameterSource in, SimpleJdbcCall jdbcCall) {
        Map<String, Object> m = jdbcCall.execute(in);
        return getJdbcResult(m, MULTIPLE_RESULT);
    }

    Page<T> withPageResultSet(SqlParameterSource in, SimpleJdbcCall jdbcCall) {
        Map<String, Object> m = jdbcCall.execute(in);
        List<T> content = getJdbcResult(m, MULTIPLE_RESULT);
        Long count = ((List<Long>) m.get(RESULT_COUNT)).get(0);
        return new Page<>(count, content);
    }

    int withReturnValue(SqlParameterSource in, SimpleJdbcCall jdbcCall) {
        Map<String, Object> m = jdbcCall.execute(in);
        return (int) m.get("RETURN_VALUE");
    }

    List<T> getJdbcResult(Map<String, Object> m, String resultSet) {
        return (List<T>) m.get(resultSet);
    }


    String buildDynamicWhereClause(Map<String, String> params) {
        return "";
    }

}