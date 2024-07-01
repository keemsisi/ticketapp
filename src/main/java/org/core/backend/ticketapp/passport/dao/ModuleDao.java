package org.core.backend.ticketapp.passport.dao;


import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.entity.UserModule;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class ModuleDao extends BaseDao {

    @Autowired
    DataSource dataSource;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public Optional<Set<UserModule>> getUserModulesByModuleId(UUID moduleId) {
        String sql = "SELECT um.id as id, um.module_id, um.user_id, m.* FROM user_module um INNER JOIN module m ON m.id = um.module_id WHERE um.module_id = ? ";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserModule.class);
        var users = getJdbcTemplate().query(sql, rowMapper, moduleId);
        return Optional.of(new HashSet<>(users));
    }

}
