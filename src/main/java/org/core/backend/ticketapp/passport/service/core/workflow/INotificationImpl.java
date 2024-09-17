package org.core.backend.ticketapp.passport.service.core.workflow;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.stereotype.Service;

import javax.mail.Message;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class INotificationImpl implements INotification {
    @Override
    public Page<Notification> getWorkflowNotificationWithStatus(ApprovalStatus status, UUID uuid, Pageable pageable) {
        return null;
    }

    @Override
    public void approveRequest(SingleRequestApprovalDto singleRequestApprovalDto) throws Exception {

    }

    @Override
    public List<Notification> getWorkflowNotificationWithStatus(ApprovalStatus status, UUID moduleId) {
        return List.of();
    }

    @Override
    public void sendNotificationToClients(List<User> userUUuids, Message message) {

    }

    @Override
    public void pushNotificationToPermittedClients(Notification notification) {

    }

    @Override
    public Object getAllByActionNameStatusAndModuleIdPaged(String actionName, ApprovalStatus status, UUID moduleId, Date startDate, Date endDate, PageRequest dateCreated) {
        return null;
    }

    @Override
    public Object getAllByActionNameStatusAndModuleIdUnPaged(String actionName, ApprovalStatus status, Date startDate, Date endDate, UUID moduleId) {
        return null;
    }

    @Override
    public void notificationBulkApproval(BulkRequestApprovalDto bulkRequestApprovalDto) {

    }

    @Override
    public void readNotification(NotificationReadDTO notificationReadDTO) {

    }

    @Override
    public Object getUserNotificationsByUserIdTenantId(PageRequest dateCreated, Sort.Direction order) {
        return null;
    }

    @Override
    public Object getUserUnreadNotificationsStatsByModuleId(UUID moduleId) {
        return null;
    }

    @Override
    public Notification getNotificationById(UUID id) {
        return null;
    }

    @Override
    public UserNotificationApprovalStatusStats getUserNotificationApprovalStatusStats() {
        return null;
    }

    @Override
    public List<UserNotificationModuleStatsWrapper> getUserModuleStats() {
        return List.of();
    }

    @Override
    public List<UserNotificationGroupByDateCreatedWrapper> fetchUserNotificationsReceivedByDateRange(Date start, Date end) {
        return List.of();
    }
}
