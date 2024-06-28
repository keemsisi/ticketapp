package org.core.backend.ticketapp.passport.mapper;

import com.zaxxer.q2o.Q2Obj;
import io.jsonwebtoken.lang.Collections;
import org.core.backend.ticketapp.passport.entity.Client;
import org.postgresql.jdbc.PgArray;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ClientRowMapper implements RowMapper<Client> {

	@Override
	public Client mapRow(ResultSet resultSet, int i) throws SQLException {
		var client = Q2Obj.fromResultSet(resultSet, new Client());
		client.setAdditionalInformation((Map<String, Object>)resultSet.getObject("additional_information"));
		client.setGrantTypes(Collections.arrayToList(((PgArray)resultSet.getObject("grant_types")).getArray()));
		client.setRedirectUri(Collections.arrayToList(((PgArray)resultSet.getObject("redirect_uri")).getArray()));
		client.setDomains(Collections.arrayToList(((PgArray)resultSet.getObject("domains")).getArray()));
		client.setResources(Collections.arrayToList(((PgArray)resultSet.getObject("resources")).getArray()));
		client.setGrantedAuthorities(Collections.arrayToList(((PgArray)resultSet.getObject("authorities")).getArray()));
		client.setScopes(Collections.arrayToList(((PgArray)resultSet.getObject("scope")).getArray()));
		return client;
	}
}