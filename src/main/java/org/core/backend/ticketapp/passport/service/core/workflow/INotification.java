package org.core.backend.ticketapp.passport.service.core.workflow;


import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.passport.dtos.notification.BulkRequestApprovalDto;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationReadDTO;
import org.core.backend.ticketapp.passport.dtos.notification.SingleRequestApprovalDto;
import org.core.backend.ticketapp.passport.dtos.notification.UserNotificationApprovalStatusStats;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationModuleStatsWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.mail.Message;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface INotification {
    Page<Notification> getWorkflowNotificationWithStatus(ApprovalStatus status, UUID uuid, Pageable pageable);

    void approveRequest(SingleRequestApprovalDto singleRequestApprovalDto) throws Exception;

    List<Notification> getWorkflowNotificationWithStatus(ApprovalStatus status, UUID moduleId);

    void sendNotificationToClients(List<User> userUUuids, Message message);

    void pushNotificationToPermittedClients(Notification notification);

    Object getAllByActionNameStatusAndModuleIdPaged(String actionName, ApprovalStatus status, UUID moduleId, Date startDate, Date endDate, PageRequest dateCreated);

    Object getAllByActionNameStatusAndModuleIdUnPaged(String actionName, ApprovalStatus status, Date startDate, Date endDate, UUID moduleId);

    void notificationBulkApproval(BulkRequestApprovalDto bulkRequestApprovalDto);

    void readNotification(NotificationReadDTO notificationReadDTO);

    Object getUserNotificationsByUserIdTenantId(PageRequest dateCreated, Sort.Direction order);

    Object getUserUnreadNotificationsStatsByModuleId(UUID moduleId);

    Notification getNotificationById(UUID id);

    UserNotificationApprovalStatusStats getUserNotificationApprovalStatusStats();

    List<UserNotificationModuleStatsWrapper> getUserModuleStats();

    List<UserNotificationGroupByDateCreatedWrapper> fetchUserNotificationsReceivedByDateRange(Date start, Date end);
}
