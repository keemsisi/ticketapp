package org.core.backend.ticketapp.passport.dao;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.dtos.core.GroupActionsDto;
import org.core.backend.ticketapp.passport.dtos.core.UserAvatar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class GroupDao extends BaseDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public List<GroupActionsDto> getGroupActionsByGroupId(UUID groupId) {
        String sql = "SELECT ga.action_id, a.code, a.name, a.description FROM group_action ga LEFT JOIN action a ON a.id = ga.action_id WHERE ga.group_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(GroupActionsDto.class);
        var users = getJdbcTemplate().query(sql, rowMapper, groupId);
        return users;
    }

    public List<UserAvatar> getGroupUsersByRoleId(UUID groupId) {
        String sql = "select gu.user_id, u.first_name, u.last_name , u.email,  u.profile_picture_location from group_user gu, users u  where gu.user_id = u.id and gu.group_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserAvatar.class);
        var users = getJdbcTemplate().query(sql, rowMapper, groupId);
        return new ArrayList<>(users);
    }

}
