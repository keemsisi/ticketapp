package org.core.backend.ticketapp.passport.dao;


import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.dtos.core.UserAvatar;
import org.core.backend.ticketapp.passport.entity.Role;
import org.core.backend.ticketapp.passport.mapper.ListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class RoleDao extends BaseDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public List<UserAvatar> getRoleUsersByRoleId(UUID roleId) {
        String sql = "select ur.user_id, u.first_name, u.last_name , u.email,  u.profile_picture_location from user_role ur, users u  where ur.user_id = u.id and ur.role_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserAvatar.class);
        var users = getJdbcTemplate().query(sql, rowMapper, roleId);
        return new ArrayList<>(users);
    }

    public List<String> getRoleUsersByRoleId(List<UUID> roleIds) {
        String inSql = String.join(",", Collections.nCopies(roleIds.size(), "?"));
        String sql = "select ro.code from role ro where ro.id IN (%s)";
        var users = getJdbcTemplate().query(String.format(sql, inSql), new ListMapper(), roleIds);
        return users.get(0);
    }

    public Role getRole(UUID roleId) {
        String sql = "select * from role ro where ro.id = ? ";
        var roles = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Role.class), roleId);
        return roles.get(0);
    }

}
