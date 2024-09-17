package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Notification;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            "WHERE module_id=?2 AND n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.tenant_id=?3", nativeQuery = true)
    Page<Notification> getAllByStatusAndModuleId(String approvalStatus, UUID moduleId, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?1 AND n.tenant_id=?2", nativeQuery = true)
    Page<Notification> getAllByModuleId(UUID moduleId, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "AND n.tenant_id=?1", nativeQuery = true)
    Page<Notification> getAllByTenantIdPaged(UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?1 AND n.tenant_id=?2", nativeQuery = true)
    List<Notification> getAllByModuleId(UUID moduleId, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.tenant_id=?1", nativeQuery = true)
    List<Notification> getAllByTenantId(UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.tenant_id=?3", nativeQuery = true)
    List<Notification> getAllByStatusAndModuleId(String status, UUID moduleId, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.tenant_id=?2 AND n.notification_type = ?3 ", nativeQuery = true)
    List<Notification> getAllByStatus(String status, UUID tenantId, String notificationType);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.tenant_id=?2 AND n.notification_type = ?3 ", nativeQuery = true)
    Page<Notification> getAllByStatus(String status, UUID tenantId, String notificationType, Pageable pageable);

    @Deprecated
    @Modifying
    @Query(value = "UPDATE notification SET new_data=CAST(?2 AS jsonb), old_data=CAST(?3 AS jsonb), meta_data=CAST(?4 AS jsonb), approval_status=?5, date_approved=CAST(?6 AS TIMESTAMP), date_declined=CAST(?7 AS TIMESTAMP), workflow=CAST(?8 AS jsonb) WHERE id=?1", nativeQuery = true)
    void update(UUID id, String newData, String oldData, String metaData, String approvalStatus, LocalDateTime dateApproved, LocalDateTime dateDeclined, String workflow);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?3 AND n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.tenant_id=?4", nativeQuery = true)
    List<Notification> getAllByActionNameAndStatusAndModuleId(String actionName, String approvalStatus, UUID moduleId, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.tenant_id=?3", nativeQuery = true)
    List<Notification> getAllByActionNameAndStatus(String actionName, String approvalStatus, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?3 AND n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.tenant_id=?4", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStatusAndModuleIdPaged(String actionName, String approvalStatus, UUID moduleId, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.tenant_id=?3", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStatusPaged(String actionName, String approvalStatus, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.action_name=?1 AND n.tenant_id=?3", nativeQuery = true)
    List<Notification> getAllByActionNameAndModuleId(String actionName, UUID moduleId, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.tenant_id=?2", nativeQuery = true)
    List<Notification> getAllByActionName(String actionName, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.action_name=?1 AND n.tenant_id=?3", nativeQuery = true)
    Page<Notification> getAllByActionNameAndModuleId(String actionName, UUID moduleId, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.tenant_id=?2", nativeQuery = true)
    Page<Notification> getAllByActionName(String actionName, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT action_name FROM notification WHERE id IN ?1 AND tenant_id=?2", nativeQuery = true)
    List<String> getActionNamesByNotificationIds(List<UUID> notificationId, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE NOT EXISTS (SELECT 1 FROM read_notification rn WHERE rn.user_id=?1 AND n.id =rn.notification_id) " +
            "AND n.action_name IN ?2 AND n.tenant_id=?3 ORDER BY n.date_created DESC LIMIT 25", nativeQuery = true)
    List<Notification> getAllUserUnreadNotificationsUnPaged(UUID userId, List<String> userPermissions, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE NOT EXISTS (SELECT 1 FROM read_notification rn WHERE rn.user_id=?1 AND n.id =rn.notification_id) " +
            "AND n.action_name IN ?2 AND n.tenant_id=?3", nativeQuery = true)
    Page<Notification> getAllUserUnreadNotificationsPaged(UUID userId, List<String> userPermissions, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?3 AND n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.date_created >= CAST(?4 AS TIMESTAMP) AND n.date_created <= CAST(?5 AS TIMESTAMP) AND n.tenant_id=?6", nativeQuery = true)
    List<Notification> getAllByActionNameAndStatusAndModuleIdAndStartDateAndEndDateUnPaged(String actionName, String status, UUID moduleId, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) AND n.tenant_id=?5", nativeQuery = true)
    List<Notification> getAllByActionNameAndStatusAndStartDateAndEndDateUnPaged(String actionName, String status, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.action_name=?1 AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) AND n.tenant_id=?5", nativeQuery = true)
    List<Notification> getAllByActionNameAndModuleIdAndStartDateAndEndDateUnPaged(String actionName, UUID moduleId, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) AND n.tenant_id=?4", nativeQuery = true)
    List<Notification> getAllByActionNameAndStartDateAndEndDateUnPaged(String actionName, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) AND n.tenant_id=?5", nativeQuery = true)
    List<Notification> getAllByStatusAndModuleIdAndStartDateAndEndDateUnPaged(String status, UUID moduleId, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) AND n.tenant_id=?4", nativeQuery = true)
    List<Notification> getAllByStatusAndStartDateAndEndDateUnPaged(String status, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?3 AND n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.date_created >= CAST(?4 AS TIMESTAMP) AND n.date_created <= CAST(?5 AS TIMESTAMP) AND n.tenant_id=?6", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStatusAndModuleIdAndStartDateAndEndDatePaged(String actionName, String approvalStatus, UUID moduleId, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.approval_status=CAST(?2 AS workflow_approval_status_enum) AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) AND n.tenant_id=?5", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStatusAndStartDateAndEndDatePaged(String actionName, String approvalStatus, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.action_name=?1 AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) AND n.tenant_id=?5", nativeQuery = true)
    Page<Notification> getAllByActionNameAndModuleIdAndStartDateAndEndDatePaged(String actionName, UUID moduleId, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.action_name=?1 AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) AND n.tenant_id=?4", nativeQuery = true)
    Page<Notification> getAllByActionNameAndStartDateAndEndDatePaged(String actionName, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?2 AND n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.date_created >= CAST(?3 AS TIMESTAMP) AND n.date_created <= CAST(?4 AS TIMESTAMP) AND n.tenant_id=?5", nativeQuery = true)
    Page<Notification> getAllStatusAndModuleIdAndStartDateAndEndDatePaged(String status, UUID moduleId, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.approval_status=CAST(?1 AS workflow_approval_status_enum) AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) AND n.tenant_id=?4", nativeQuery = true)
    Page<Notification> getAllStatusAndStartDateAndEndDatePaged(String status, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?1 AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) AND n.tenant_id=?4", nativeQuery = true)
    List<Notification> getAllModuleIdAndStartDateAndEndDateUnPaged(UUID moduleId, Date startDate, Date endDate, UUID tenantId);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.date_created >= CAST(?1 AS TIMESTAMP) AND n.date_created <= CAST(?2 AS TIMESTAMP) AND n.tenant_id=?3 AND n.notification_type = ?4", nativeQuery = true)
    List<Notification> getAllStartDateAndEndDateUnPaged(Date startDate, Date endDate, UUID tenantId, String notificationType);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE module_id=?1 AND n.date_created >= CAST(?2 AS TIMESTAMP) AND n.date_created <= CAST(?3 AS TIMESTAMP) AND n.tenant_id=?4", nativeQuery = true)
    Page<Notification> getAllModuleIdAndStartDateAndEndDatePaged(UUID moduleId, Date startDate, Date endDate, UUID tenantId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name, sa.sms_alert AS should_send_sms_alert, sa.email_alert AS should_send_email_alert, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN system_alert sa ON n.tenant_id=sa.tenant_id " +
            "WHERE n.date_created >= CAST(?1 AS TIMESTAMP) AND n.date_created <= CAST(?2 AS TIMESTAMP) AND n.tenant_id=?3 AND n.notification_type = ?4", nativeQuery = true)
    Page<Notification> getAllStartDateAndEndDatePaged(Date startDate, Date endDate, UUID tenantId, String notificationType, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name,CONCAT(u2.first_name,' ',u2.last_name) AS notification_for_user_name, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN users u2 ON u2.id=notification_for_user_id " +
            "WHERE n.notification_for_user_id=:userId AND n.notification_type='RELIEF_REQUEST'", nativeQuery = true)
    Page<Notification> getAllReliefRequestForUserNotification(UUID userId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name,CONCAT(u2.first_name,' ',u2.last_name) AS notification_for_user_name, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN users u2 ON u2.id=notification_for_user_id " +
            "WHERE n.requested_by=:userId AND n.notification_type='RELIEF_REQUEST'", nativeQuery = true)
    Page<Notification> getAllReliefRequestByUserNotification(UUID userId, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name,CONCAT(u2.first_name,' ',u2.last_name) AS notification_for_user_name, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN users u2 ON u2.id=notification_for_user_id " +
            "WHERE n.notification_for_user_id=:userId AND n.approval_status=CAST(:approvalStatus AS workflow_approval_status_enum) AND n.notification_type='RELIEF_REQUEST'", nativeQuery = true)
    Page<Notification> getAllReliefRequestNotificationForUserWithStatus(UUID userId, String approvalStatus, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name,CONCAT(u2.first_name,' ',u2.last_name) AS notification_for_user_name, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN users u2 ON u2.id=notification_for_user_id " +
            "WHERE n.requested_by=:userId AND n.approval_status=CAST(:approvalStatus AS workflow_approval_status_enum) AND n.notification_type='RELIEF_REQUEST'", nativeQuery = true)
    Page<Notification> getAllReliefRequestNotificationByUserWithStatus(UUID userId, String approvalStatus, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name,CONCAT(u2.first_name,' ',u2.last_name) AS notification_for_user_name, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN users u2 ON u2.id=notification_for_user_id " +
            "WHERE n.requested_by=:requestedById AND n.approval_status=CAST(:approvalStatus AS workflow_approval_status_enum) AND n.notification_type='RELIEF_REQUEST'", nativeQuery = true)
    Page<Notification> getAllReliefRequestNotificationByRequestedByIdWithStatus(UUID requestedById, String approvalStatus, Pageable pageable);

    @Query(value = "SELECT CONCAT(u.first_name,' ',u.last_name) AS requested_by_name,CONCAT(u2.first_name,' ',u2.last_name) AS notification_for_user_name, n.* FROM notification n " +
            "LEFT JOIN users u ON u.id = n.requested_by " +
            "LEFT JOIN users u2 ON u2.id=notification_for_user_id " +
            "WHERE n.requested_by=:requestedById AND n.notification_type='RELIEF_REQUEST'", nativeQuery = true)
    Page<Notification> getAllReliefRequestNotificationByRequestedById(UUID requestedById, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE notification SET processed_in_app_status_update=true, date_modified=CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
    void updateProcessedPushNotificationStatusUpdate(UUID notificationId);
}