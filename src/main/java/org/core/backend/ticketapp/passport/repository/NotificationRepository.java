package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>, PagingAndSortingRepository<Notification, UUID> {
    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.id=?1", nativeQuery = true)
    Optional<Notification> findByUUID(@NotNull UUID id);


    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.notification_type = ?3 ", nativeQuery = true)
    List<Notification> getAllByStatus(String status, String notificationType);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.notification_type = ?3 ", nativeQuery = true)
    Page<Notification> getAllByStatus(String status, String notificationType, Pageable pageable);

    @Deprecated
    @Modifying
    @Query(value = "UPDATE notification SET new_data=CAST(?2 AS jsonb), old_data=CAST(?3 AS jsonb), meta_data=CAST(?4 AS jsonb), approval_status=?5, date_approved=CAST(?6 AS TIMESTAMP), date_declined=CAST(?7 AS TIMESTAMP), workflow=CAST(?8 AS jsonb) WHERE id=?1", nativeQuery = true)
    void update(UUID id, String newData, String oldData, String metaData, String approvalStatus, LocalDateTime dateApproved, LocalDateTime dateDeclined, String workflow);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) ", nativeQuery = true)
    List<Notification> getAllByActionNameAndStatus(String actionName, String approvalStatus);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) ", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStatusPaged(String actionName, String approvalStatus, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 ", nativeQuery = true)
    List<Notification> getAllByActionName(String actionName);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 ", nativeQuery = true)
    Page<Notification> getAllByActionName(String actionName, Pageable pageable);

    @Query(value = "SELECT action_name FROM notification WHERE id IN ?1 ", nativeQuery = true)
    List<String> getActionNamesByNotificationIds(List<UUID> notificationId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE NOT EXISTS (SELECT 1 FROM read_notification rn WHERE rn.user_id=?1 AND n.id =rn.notification_id) " +
            "AND n.action_name IN ?2 ORDER BY n.date_created DESC LIMIT 25", nativeQuery = true)
    List<Notification> getAllUserUnreadNotificationsUnPaged(UUID userId, List<String> userPermissions);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE NOT EXISTS (SELECT 1 FROM read_notification rn WHERE rn.user_id=?1 AND n.id =rn.notification_id) " +
            "AND n.action_name IN ?2 ", nativeQuery = true)
    Page<Notification> getAllUserUnreadNotificationsPaged(UUID userId, List<String> userPermissions, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) ", nativeQuery = true)
    List<Notification> getAllByActionNameAndStatusAndStartDateAndEndDateUnPaged(String actionName, String status, Date startDate, Date endDate);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) ", nativeQuery = true)
    List<Notification> getAllByActionNameAndStartDateAndEndDateUnPaged(String actionName, Date startDate, Date endDate);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) ", nativeQuery = true)
    List<Notification> getAllByStatusAndStartDateAndEndDateUnPaged(String status, Date startDate, Date endDate);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) ", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStatusAndStartDateAndEndDatePaged(String actionName, String approvalStatus, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) ", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStartDateAndEndDatePaged(String actionName, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) ", nativeQuery = true)
    Page<Notification> getAllStatusAndStartDateAndEndDatePaged(String status, Date startDate, Date endDate, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.date_created >= CAST(?1 AS TIMESTAMP) AND n.date_created <= CAST(?2 AS TIMESTAMP) AND n.notification_type = ?4", nativeQuery = true)
    List<Notification> getAllStartDateAndEndDateUnPaged(Date startDate, Date endDate, String notificationType);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.date_created >= CAST(?1 AS TIMESTAMP) AND n.date_created <= CAST(?2 AS TIMESTAMP) AND n.notification_type = ?4", nativeQuery = true)
    Page<Notification> getAllStartDateAndEndDatePaged(Date startDate, Date endDate, String notificationType, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE notification SET processed_in_app_status_update=true, date_modified=CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void updateProcessedPushNotificationStatusUpdate(UUID notificationId);
}