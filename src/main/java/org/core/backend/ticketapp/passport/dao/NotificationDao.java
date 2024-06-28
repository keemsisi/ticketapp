package org.core.backend.ticketapp.passport.dao;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.core.backend.ticketapp.passport.dtos.PageRequestParam;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationIdDTOMap;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.entity.WebSocketPushNotification;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.core.backend.ticketapp.passport.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional
public class NotificationDao implements INotificationDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Deprecated
    public List<User> filterAllowedUsers(String actionName, UUID tenantId, UUID moduleId) {
        String sql = "SELECT u.* FROM \"users\" u INNER JOIN (SELECT ua.user_id FROM \"action\" a\n" +
                "INNER JOIN user_action ua \n" +
                "\tON ua.action_id=a.id \n" +
                "\tWHERE exists ( \n" +
                "\t\tSELECT ns.user_id FROM notification_subscriber ns \n" +
                "\t\t\tWHERE ua.user_id=ns.user_id \n" +
                "\t\t\tAND ns.active=true \n" +
                "\t\t\tAND ns.unsubscribed=false\n" +
                "\t) AND a.code=? AND a.tenant_id=?\n" +
                "\tAND a.module_id=?) AS t ON t.user_id=u.id;";
        var rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        return jdbcTemplate.query(sql, rowMapper, actionName, tenantId, moduleId);
    }

    @Override
    public List<NotificationSubscriber> getAllMatchingSubscriberCurrentSessionIds(String actionName) {
        String sql = "SELECT current_session_id FROM notification_subscriber \n" +
                "WHERE subscriber_scope @> ARRAY[?]::text[] \n" +
                "AND unsubscribed=false AND active=true \n";
        var rowMapper = BeanPropertyRowMapper.newInstance(NotificationSubscriber.class);
        return jdbcTemplate.query(sql, rowMapper, actionName);
    }

    @Override
    public Map<String, Object> getUserNotificationStats(List<String> actionNames, List<String> roles, UUID userId) {
        String actions = CommonUtils.getDelimitedActionNamesWithSingleQuotes(actionNames);
        String roles_ = CommonUtils.getDelimitedActionNamesWithSingleQuotes(roles);
        StringBuilder sqlQueryBuilder = new StringBuilder();
        var sent = String.format("SELECT COUNT(requested_by) FROM notification n \n" +
                "WHERE n.action_name IN (%s) AND requested_by='%s' GROUP BY requested_by;", actions, userId);
        var sql = String.format("SELECT COUNT(items.\"approvalStatus\") from notification n , jsonb_to_recordset(n.workflow) AS items(\"approvalStatus\" workflow_approval_status_enum, \"roleCode\" text) WHERE items.\"approvalStatus\"='PENDING' AND items.\"roleCode\" IN (%s);", roles_) +
                String.format("SELECT COUNT(n.approval_status) FROM notification n WHERE workflow is NULL AND approval_status = 'PENDING' AND n.action_name IN (%s);", actions) +
                String.format("SELECT COUNT(items.\"approvedBy\") from notification n , jsonb_to_recordset(n.workflow) \n" +
                        "AS items(\"approvedBy\" text,\"approvalStatus\" workflow_approval_status_enum) where items.\"approvedBy\" = '%s' AND items.\"approvalStatus\"='REJECTED';", userId) +
                String.format("SELECT COUNT(items.\"approvedBy\") FROM notification n , jsonb_to_recordset(n.workflow) \n" +
                        "AS items(\"approvedBy\" text,\"approvalStatus\" workflow_approval_status_enum) WHERE items.\"approvedBy\" = '%s' AND items.\"approvalStatus\"='DECLINED';", userId) +
                String.format("SELECT COUNT(items.\"approvedBy\") from notification n , jsonb_to_recordset(n.workflow) \n" +
                        "AS items(\"approvedBy\" text,\"approvalStatus\" workflow_approval_status_enum) WHERE items.\"approvedBy\" = '%s' AND items.\"approvalStatus\"='APPROVED';", userId);
        sqlQueryBuilder
                .append(sent)
                .append(sql);
        var cscFactory = new CallableStatementCreatorFactory(sqlQueryBuilder.toString());
        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("sent", BeanPropertyRowMapper.newInstance(LongWrapper.class)),
                new SqlReturnResultSet("pending_by_role_code", BeanPropertyRowMapper.newInstance(LongWrapper.class)),
                new SqlReturnResultSet("pending_by_action_name", BeanPropertyRowMapper.newInstance(LongWrapper.class)),
                new SqlReturnResultSet("rejected", BeanPropertyRowMapper.newInstance(LongWrapper.class)),
                new SqlReturnResultSet("declined", BeanPropertyRowMapper.newInstance(LongWrapper.class)),
                new SqlReturnResultSet("approved", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        return jdbcTemplate.call(csc, returnedParams);
    }


    @Override
    public List<UserNotificationGroupByDateCreatedWrapper> getUserNotificationsReceivedByDateRange(List<String> actionNames, Date startDate, Date endDate) {
        var rowMapper = BeanPropertyRowMapper.newInstance(UserNotificationGroupByDateCreatedWrapper.class);
        String actions = CommonUtils.getDelimitedActionNamesWithSingleQuotes(actionNames);
        var moduleStatsSql = String.format("SELECT date_created::TIMESTAMPTZ::DATE, COUNT(date_created::TIMESTAMPTZ::DATE) FROM notification n " +
                "WHERE n.action_name IN (%s) " +
                "AND date_created::TIMESTAMPTZ::DATE >= CAST(? AS DATE) AND date_created::TIMESTAMPTZ::DATE <= CAST(? AS DATE) GROUP BY date_created::TIMESTAMPTZ::DATE;", actions);
        return jdbcTemplate.query(moduleStatsSql, rowMapper, startDate, endDate);
    }


    /*TODO : Get user read and unread notification */
    /*TODO : Get user read and unread notification paginated*/
    /*TODO : Get user read notification paginated*/
    /*TODO : Get user unread notification paginated --- ongoing*/
    @Override
    @Deprecated
    public Page<Notification> getUserUnreadNotificationPaged(UUID userId, Pageable pageable, Sort.Direction direction) {
        int limit = pageable.getPageSize();
        Long offSet = pageable.getOffset();
        String orderDirection = direction.name();
        String sql = String.format("SELECT n.*, CONCAT(u.first_name,u.last_name) AS requested_by_name FROM notification n\n" +
                "LEFT JOIN users u ON u.id = n.requested_by\n" +
                "INNER JOIN user_module um ON um.module_id = n.module_id WHERE um.user_id='%s' \n" +
                "AND NOT EXISTS (SELECT 1 FROM read_notification rn WHERE rn.user_id='%s' AND n.id =rn.notification_id) ORDER BY date_created %s LIMIT %s OFFSET %s;", userId, userId, orderDirection, limit, offSet) + String.format("SELECT COUNT(*) FROM (SELECT n.* FROM notification n " +
                "INNER JOIN user_module um ON um.module_id = n.module_id WHERE um.user_id='%s' " +
                "AND NOT exists (SELECT 1 FROM read_notification rn WHERE rn.user_id='%s' AND n.id =rn.notification_id)) as t", userId, userId);

        var cscFactory = new CallableStatementCreatorFactory(sql);
        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("user_unread_notifications", BeanPropertyRowMapper.newInstance(Notification.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        Map<String, Object> results = jdbcTemplate.call(csc, returnedParams);
        return PageableExecutionUtils.getPage((List<Notification>) results.get("user_unread_notifications"), pageable, () -> ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount());
    }

    @Deprecated
    public List<Notification> getUserUnreadNotificationUnPaged(UUID userId) {
        var rowMapper = BeanPropertyRowMapper.newInstance(Notification.class);
        String sql = "SELECT n.*, CONCAT(u.first_name,u.last_name) AS requested_by_name FROM notification n\n" +
                "LEFT JOIN users u ON u.id = n.requested_by\n" +
                "INNER JOIN user_module um ON um.module_id = n.module_id WHERE um.user_id=? \n" +
                "AND NOT EXISTS (SELECT 1 FROM read_notification rn WHERE rn.user_id=? AND n.id =rn.notification_id);";
        return jdbcTemplate.query(sql, rowMapper, userId, userId);
    }

    @Override
    public int updateNotificationProcessor(UUID notificationId, String remark, String status, LocalDateTime startDate, LocalDateTime endDate) {
        String endDateStr = endDate == null ? " " : String.format(",processor_end_date= '%s' ", endDate);
        String sql = String.format("UPDATE notification SET processor_remark = '%s' , " +
                " processor_status= '%s', processor_start_date = '%s', date_modified = CURRENT_TIMESTAMP " +
                " %s WHERE id = '%s' ", remark, status, startDate, endDateStr, notificationId);
        return jdbcTemplate.update(sql);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Notification> getAllNotifications(UUID userId, List<String> userRoles, PageRequestParam prp) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<NotificationType> notificationTypes = List.of(NotificationType.IN_APP, NotificationType.REMINDER, NotificationType.RELIEF_REQUEST);
        @NotNull final String ORDER_BY = ObjectUtils.isEmpty(prp.getSortBy()) ? "index DESC" : Arrays.stream(prp.getSortBy())
                .map(column -> column + " " + prp.getOrder().name())
                .collect(Collectors.joining(" ,"));
        @NotNull Integer SIZE = ObjectUtils.isEmpty(prp.getSize()) ? 50 : prp.getSize();

        var query = new StringBuilder("SELECT u.first_name, u.middle_name ,u.last_name WHERE 1=1 ");

        if (ObjectUtils.isNotEmpty(prp.getActionName()))
            query.append(String.format("AND n.action_name='%s' ", prp.getActionName().toLowerCase()));

        if (ObjectUtils.isNotEmpty(prp.getNotificationId()))
            query.append(String.format("AND n.id='%s' ", prp.getNotificationId()));

        if (ObjectUtils.isNotEmpty(prp.getClientCompanyId()))
            query.append(" AND n.new_data::varchar LIKE '%").append(prp.getClientCompanyId()).append("%' ");

        if (ObjectUtils.isNotEmpty(prp.getStatus()))
            query.append(String.format("AND n.approval_status ='%s' ", prp.getStatus()));

        if (ObjectUtils.isNotEmpty(prp.getModuleId()))
            query.append(String.format("AND n.module_id ='%s' ", prp.getModuleId()));

        if (ObjectUtils.isNotEmpty(prp.getRequestedById()))
            query.append(String.format("AND n.requested_by ='%s' ", prp.getRequestedById()));

        if (ObjectUtils.isNotEmpty(prp.getStartDate()) && ObjectUtils.isEmpty(prp.getEndDate()))
            query.append(String.format("AND n.date_created >= '%s' ", prp.getStartDate()));

        if (ObjectUtils.isNotEmpty(prp.getStartDate()) && ObjectUtils.isNotEmpty(prp.getEndDate()))
            query.append(String.format("AND n.date_created BETWEEN '%s' AND '%s' ", prp.getStartDate(), prp.getEndDate()));

        if (ObjectUtils.isNotEmpty(prp.getAccountNumber())) {
            query.append(" AND ( n.new_data::varchar ILIKE '%").append(prp.getAccountNumber()).append("%' ");
            query.append(" OR n.old_data::varchar ILIKE '% ").append(prp.getAccountNumber()).append("%') ");
        }

        if (ObjectUtils.isNotEmpty(prp.getSearch())) {
            query.append(" AND ( n.new_data::varchar ILIKE '%").append(prp.getSearch()).append("%' ");
            query.append(" OR n.old_data::varchar ILIKE '% ").append(prp.getSearch()).append("%') ");
        }

        //You have to be super admin to check other's notification
        if (prp.isForUserId()) {
            if (ObjectUtils.isNotEmpty(prp.getType()))
                query.append(String.format(" AND notification_type='%s' ", prp.getType()));
            if (ObjectUtils.isNotEmpty(prp.getType()) && notificationTypes.contains(prp.getType()))
                query.append(String.format(" AND notification_type='%s' ", prp.getType()));
            if (ObjectUtils.isNotEmpty(prp.getActionName()))
                query.append(String.format(" AND n.notification_for_user_id='%s' AND n.action_name='%s' ", userId, prp.getActionName()));
            query.append(String.format(" AND n.notification_for_user_id='%s' ", userId));
        } else if (ObjectUtils.isEmpty(prp.getNotificationForUserId()) && ObjectUtils.isEmpty(prp.getNotificationByUserId())) {
            if (ObjectUtils.isNotEmpty(prp.getType()) && !notificationTypes.contains(prp.getType()))
                query.append(String.format("  AND n.notification_type='%s' ", prp.getType()));
            else if (ObjectUtils.isNotEmpty(prp.getType()) && notificationTypes.contains(prp.getType()))
                query.append(String.format("  AND n.notification_type='%s' AND n.notification_for_user_id='%s' ", prp.getType(), userId));
            else query.append(String.format(" AND n.notification_type='%s' ", NotificationType.SUBSCRIPTION.name()));
        } else if (userRoles.contains("super_admin")) {
            if (ObjectUtils.isNotEmpty(prp.getNotificationForUserId()))
                query.append(String.format(" AND n.notification_for_user_id='%s' ", prp.getNotificationForUserId()));
            if (ObjectUtils.isNotEmpty(prp.getNotificationByUserId()))
                query.append(String.format(" AND n.requested_by='%s' ", prp.getNotificationByUserId()));
            if (ObjectUtils.isNotEmpty(prp.getType()))
                query.append(String.format(" AND n.notification_type='%s' ", prp.getType()));
        }

        if (ObjectUtils.isNotEmpty(prp.getStartDateApproved())) {
            query.append(String.format(" AND n.date_approved >= '%s' ", prp.getStartDateApproved()));
        }

        if (ObjectUtils.isNotEmpty(prp.getStartDateApproved()) && ObjectUtils.isNotEmpty(prp.getEndDateApproved())) {
            query.append(String.format(" AND n.date_approved BETWEEN '%s' AND '%s' ", prp.getStartDateApproved(), prp.getEndDateApproved()));
        }

        query.append((ObjectUtils.isEmpty(prp.getIndex()) ? 0 : prp.getIndex()) <= 0 ? " " : String.format("AND n.index < %s ", prp.getIndex()));
        query.append(String.format(" ORDER BY %s LIMIT %s ", ORDER_BY, SIZE));

        var rowMapper = BeanPropertyRowMapper.newInstance(Notification.class);
        return jdbcTemplate.query(query.toString(), rowMapper);
    }

    @Override
    public List<NotificationIdDTOMap> getAllPendingPushNotificationProcessorStatusUpdate() {
        var rowMapper = BeanPropertyRowMapper.newInstance(NotificationIdDTOMap.class);
        var sqlQuery = "SELECT n.id notification_id FROM notification n WHERE n.should_send_in_app_alert=true " +
                " AND n.processed_in_app_status_update=false AND n.processor_status='COMPLETED_SUCCESSFULLY'" +
                " AND n.approval_status='APPROVED'";
        return jdbcTemplate.query(sqlQuery, rowMapper);
    }

    @Override
    public List<WebSocketPushNotification> getUnDeliveredWebSocketPushNotifications() {
        var rowMapper = BeanPropertyRowMapper.newInstance(WebSocketPushNotification.class);
        var sqlQuery = "SELECT * FROM web_socket_push_notification n WHERE n.notification_delivered=false";
        return jdbcTemplate.query(sqlQuery, rowMapper);
    }
}