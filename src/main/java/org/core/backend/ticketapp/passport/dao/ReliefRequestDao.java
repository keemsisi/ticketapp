package org.core.backend.ticketapp.passport.dao;

import org.core.backend.ticketapp.passport.entity.GroupUsers;
import org.core.backend.ticketapp.passport.entity.UserAction;
import org.core.backend.ticketapp.passport.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ReliefRequestDao implements IReliefRequestDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Map<String, Object> getReliefOfficerNotExistsRolesActionsAndGroups(UUID reliefOfficerId, UUID requestedById) {
        StringBuilder sqlQueryBuilder = new StringBuilder();
        var reliefOfficerRolesNotExists = String.format("SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE NOT EXISTS (SELECT * from user_role ur2 WHERE ur2.user_id='%s' AND ur2.role_id = ur.role_id) AND ur.user_id = '%s';", reliefOfficerId, requestedById);
        var reliefOfficerActionsNotExists = String.format("SELECT up.*, p.code FROM user_action up LEFT JOIN action p ON p.id = up.action_id WHERE NOT EXISTS (SELECT * from user_action ua WHERE ua.user_id='%s' AND ua.action_id = up.action_id) AND user_id = '%s';", reliefOfficerId, requestedById);
        var reliefOfficerGroupsNotExists = String.format("SELECT gu.*, u.first_name, u.last_name FROM group_user gu LEFT JOIN users u ON gu.user_id = u.id WHERE NOT EXISTS (SELECT * FROM group_user gu2 WHERE gu2.user_id='%s' AND gu2.group_id = gu.group_id) AND user_id = '%s';", reliefOfficerId, requestedById);
        sqlQueryBuilder
                .append(reliefOfficerRolesNotExists)
                .append(reliefOfficerActionsNotExists)
                .append(reliefOfficerGroupsNotExists);
        var cscFactory = new CallableStatementCreatorFactory(sqlQueryBuilder.toString());
        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("user_roles_not_exists", BeanPropertyRowMapper.newInstance(UserRole.class)),
                new SqlReturnResultSet("user_actions_not_exists", BeanPropertyRowMapper.newInstance(UserAction.class)),
                new SqlReturnResultSet("user_groups_not_exists", BeanPropertyRowMapper.newInstance(GroupUsers.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        return jdbcTemplate.call(csc, returnedParams);
    }

    @Override
    public void deleteAllExpiredActionsGroupsAndActions() {
        var deleteExpiredReliefOfficerUserRole = "DELETE FROM user_role WHERE CAST(expiry_date AS TIMESTAMP) <= CURRENT_TIMESTAMP;";
        var deleteExpiredReliefOfficerUserAction = "DELETE FROM user_action WHERE CAST(expiry_date AS TIMESTAMP) <= CURRENT_TIMESTAMP;";
        var deleteExpiredReliefOfficerUserGroup = "DELETE FROM group_user WHERE CAST(expiry_date AS TIMESTAMP) <= CURRENT_TIMESTAMP;";
        String sqlQueryBuilder = deleteExpiredReliefOfficerUserRole + deleteExpiredReliefOfficerUserAction + deleteExpiredReliefOfficerUserGroup;
        jdbcTemplate.update(sqlQueryBuilder);
    }
}