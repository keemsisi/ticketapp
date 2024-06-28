package org.core.backend.ticketapp.passport.dao;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.dtos.core.UserAvatar;
import org.core.backend.ticketapp.passport.entity.*;
import org.core.backend.ticketapp.passport.mapper.ListMapper;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.mapper.UserRowMapper;
import org.core.backend.ticketapp.passport.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Component
public class UserDao extends BaseDao {

    @Autowired
    DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT *" +
                " FROM users u " +
                " WHERE LOWER(users.email) = LOWER(?)";
        var rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        var user = jdbcTemplate.query(sql, rowMapper, email);
        if (user.isEmpty()) {
            throw new ApplicationException(401, "bad_credentials", "invalid username or password");
        }
        return Optional.of(user.get(0));
    }

    public Optional<User> getUserPermissions(UUID id) {
        String sql = "SELECT users.id, " +
                " array_remove(array_agg(distinct a.code), null) as user_actions, " +
                " array_remove(array_agg(distinct r.code), null) as roles, " +
                " array_remove(array_agg(distinct g.code), null) as user_groups, " +
                " array_remove(array_agg(distinct a2.code), null) as group_actions " +
                " FROM users " +
                " LEFT JOIN user_action ua on ua.user_id = users.id " +
                " LEFT JOIN action a on a.id = ua.action_id " +
                " LEFT JOIN user_role ur on ur.user_id = users.id " +
                " LEFT JOIN role r on r.id = ur.role_id " +
                " LEFT JOIN group_user gu on gu.user_id = users.id" +
                " LEFT JOIN groups g on g.id = gu.group_id " +
                " LEFT JOIN group_action ga on ga.group_id = gu.group_id " +
                " LEFT JOIN action a2 on a2.id = ga.action_id" +
                " WHERE users.id = ? " +
                " GROUP BY users.id";
        var user = jdbcTemplate.query(sql, new UserRowMapper(), id);
        return Optional.of(user.get(0));
    }

    public List<String> getUserTotalActions(UUID id) {
        String sql = "SELECT users.id, " +
                " array_remove(array_agg(distinct a.code), null) as user_actions, " +
                " array_remove(array_agg(distinct a2.code), null) as group_actions " +
                " FROM users " +
                " LEFT JOIN user_action ua on ua.user_id = users.id " +
                " LEFT JOIN action a on a.id = ua.action_id " +
                " LEFT JOIN group_user gu on gu.user_id = users.id" +
                " LEFT JOIN groups g on g.id = gu.group_id " +
                " LEFT JOIN group_action ga on ga.group_id = gu.group_id " +
                " LEFT JOIN action a2 on a2.id = ga.action_id" +
                " WHERE users.id = ? " +
                " GROUP BY users.id";
        var actions = jdbcTemplate.query(sql, new ListMapper(), id);
        return actions.get(0);
    }

    public Optional<User> getUserById(UUID id) {
        String sql = "SELECT users.*, unit.name AS unit, department.name AS department " +
                " FROM users" +
                " LEFT JOIN unit ON users.unit_id = unit.id " +
                " LEFT JOIN department ON users.department_id = department.id " +
                " WHERE users.id = '" + id.toString() + "'";
        var user = jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(User.class));
        return Optional.of(user);
    }


    public Page<User> getUsers(
            String firstName,
            String lastName,
            String middleName,
            String email,
            String gender,
            String phone,
            Pageable pageable) {
        int limit = pageable.getPageSize();
        Long offSet = pageable.getOffset();

        var sqlCondition = new StringBuilder(String.format(" WHERE 1=1 "));
        if (Objects.nonNull(firstName))
            sqlCondition.append(" AND COALESCE(LOWER(u.first_name),'') LIKE CONCAT('%', '" + firstName + "','%') ");
        if (Objects.nonNull(lastName))
            sqlCondition.append(" AND COALESCE(LOWER(u.last_name),'') LIKE CONCAT('%', '" + lastName + "','%') ");
        if (Objects.nonNull(middleName))
            sqlCondition.append(" AND COALESCE(LOWER(u.middle_name),'') LIKE CONCAT('%', '" + middleName + "','%') ");
        if (Objects.nonNull(email))
            sqlCondition.append(" AND COALESCE(u.email,'') LIKE CONCAT('%', '" + email + "','%') ");
        if (Objects.nonNull(gender))
            sqlCondition.append(" AND COALESCE(u.gender,'') LIKE CONCAT('%', '" + gender + "', '%') ");
        if (Objects.nonNull(phone))
            sqlCondition.append(" AND COALESCE(u.phone,'') LIKE CONCAT('%', '" + phone + "', '%')");

        var userQuery = "SELECT u.*, ud.name as department, uu.name as unit FROM users u ".concat(sqlCondition.toString()).concat(" LIMIT " + limit + " OFFSET " + offSet + ";");
        var pageCount = "SELECT count(*) FROM users u ".concat(sqlCondition.toString());

        var cscFactory = new CallableStatementCreatorFactory(userQuery + pageCount);

        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("users", BeanPropertyRowMapper.newInstance(User.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));

        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        Map<String, Object> results = getJdbcTemplate().call(csc, returnedParams);
        return PageableExecutionUtils.getPage((List<User>) results.get("users"), pageable, () -> ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount());
    }

    public Optional<Set<UserAction>> getUserActionsById(UUID userId) {
        String sql = "SELECT up.*, p.code FROM user_action up LEFT JOIN action p ON p.id = up.action_id WHERE user_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserAction.class);
        var users = getJdbcTemplate().query(sql, rowMapper, userId);
        return Optional.of(new HashSet<>(users));
    }

    public Optional<Set<UserRole>> getUserRolesById(UUID userId) {
        String sql = "SELECT up.*, p.code FROM user_role up LEFT JOIN role p ON p.id = up.role_id WHERE user_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserRole.class);
        var users = getJdbcTemplate().query(sql, rowMapper, userId);
        return Optional.of(new HashSet<>(users));
    }

    public Optional<Set<GroupUsers>> getUserGroupsByUserId(UUID userId) {
        String sql = "SELECT gu.*, g.code FROM group_user gu LEFT JOIN groups g ON g.id = gu.group_id WHERE gu.user_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(GroupUsers.class);
        var users = getJdbcTemplate().query(sql, rowMapper, userId);
        return Optional.of(new HashSet<>(users));
    }

    public Optional<Set<GroupUsers>> getGroupActionsByUserId(UUID userId) {
        String sql = "SELECT g.code FROM group_action ga INNER JOIN groups g ON g.id = ga.group_id" +
                " INNER JOIN group_user gu on gu.user_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(GroupUsers.class);
        var users = getJdbcTemplate().query(sql, rowMapper, userId);
        return Optional.of(new HashSet<>(users));
    }

    public Optional<Set<UserModule>> getUserModulesByUserId(UUID userId) {
        String sql = "SELECT um.id as id, module_id, user_id, r.* FROM user_module um INNER JOIN module r ON r.id = um.module_id WHERE user_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserModule.class);
        var userModuleList = getJdbcTemplate().query(sql, rowMapper, userId);
        return Optional.of(new HashSet<>(userModuleList));
    }

    public Optional<Set<UserAvatar>> getUserAvatar(UUID userId) {
        String sql = "SELECT u.id as user_id, u.first_name, u.last_name, u.email, u.profile_picture_location from users u where u.id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserAvatar.class);
        var user = getJdbcTemplate().query(sql, rowMapper, userId);
        return Optional.of(new HashSet<>(user));
    }

    public Optional<Set<UserAvatar>> getUsersByDepartment(UUID departmentId) {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.email, u.profile_picture_location from users u where u.department_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(UserAvatar.class);
        var user = getJdbcTemplate().query(sql, rowMapper, departmentId);
        return Optional.of(new HashSet<>(user));
    }

    public Optional<List<User>> getUsersByIds(List<String> ids) {
        String delimitedUserIds = CommonUtils.getDelimitedActionNamesWithSingleQuotes(ids);
        String sql = String.format("SELECT u.* FROM users u where u.id IN (%s)", delimitedUserIds);
        var rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        return Optional.of(jdbcTemplate.query(sql, rowMapper));
    }
}
