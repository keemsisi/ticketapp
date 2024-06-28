package org.core.backend.ticketapp.passport.dao;


import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.dtos.core.BasicClientDetails;
import org.core.backend.ticketapp.passport.entity.Client;
import org.core.backend.ticketapp.passport.mapper.BasicClientRowMapper;
import org.core.backend.ticketapp.passport.mapper.ClientRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.UUID;

@Slf4j
@Component
public class ClientDao extends BaseDao {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public Client getClientById(UUID clientId) {
        String sql = "SELECT * from api_client WHERE id = ?";
        var client = getJdbcTemplate().query(sql, new ClientRowMapper(), clientId);
        return client.isEmpty() ? null : client.get(0);
    }

    public BasicClientDetails getBasicClientDetailsById(UUID clientId) {
        String sql = "SELECT id, client_name, client_logo, tenant_id, client_owner, domains from api_client WHERE id = ?";
        var details = getJdbcTemplate().query(sql, new BasicClientRowMapper(), clientId);
        return details.isEmpty() ? null : details.get(0);
    }


}
