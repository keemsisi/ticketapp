package org.core.backend.ticketapp.passport.service.core.notification;



import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.passport.dtos.NotificationRequestDto;
import org.core.backend.ticketapp.passport.dtos.PageRequestParam;
import org.core.backend.ticketapp.passport.dtos.notification.BulkRequestApprovalDto;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationReadDTO;
import org.core.backend.ticketapp.passport.dtos.notification.SingleRequestApprovalDto;
import org.core.backend.ticketapp.passport.dtos.notification.UserNotificationApprovalStatusStats;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationModuleStatsWrapper;
import org.core.backend.ticketapp.passport.mapper.UserUnreadNotificationStatsByModuleId;
import org.core.backend.ticketapp.passport.socketio.dto.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface INotification {

    Page<Notification> getAllByActionNameStatusAndModuleIdPaged(String actionName, ApprovalStatus status, UUID moduleId, Date startDate, Date endDate, Pageable pageable);

    List<Notification> getAllByActionNameStatusAndModuleIdUnPaged(String actionName, ApprovalStatus status, Date startDate, Date endDate, UUID moduleId);

    void sendNotificationToClients(List<NotificationSubscriber> usersCurrentSessionIds, Message message, boolean inApp);

    Message createMessage(Notification notification);

    Message createMessageForProcessorUpdate(Notification notification);

    void sendNotificationToSingleUser(UUID userId, Notification notification);

    void pushNotificationToPermittedClients(Notification notification);

    Object getUserNotificationsByUserIdTenantId(Pageable pageable, Sort.Direction direction);

    void readNotification(@NotNull NotificationReadDTO notificationReadDTO);

    Notification getNotificationById(UUID id);

    void approveRequest(SingleRequestApprovalDto singleRequestApprovalDto) throws Exception;

    void notificationBulkApproval(BulkRequestApprovalDto bulkRequestApprovalDto) throws
            Exception;

    @SuppressWarnings("unchecked")
    UserNotificationApprovalStatusStats getUserNotificationApprovalStatusStats();

    List<UserNotificationGroupByDateCreatedWrapper> fetchUserNotificationsReceivedByDateRange(Date startDate, Date endDate);

    List<UserNotificationModuleStatsWrapper> getUserModuleStats();

    List<UserUnreadNotificationStatsByModuleId> getUserUnreadNotificationsStatsByModuleId(UUID moduleId);

    List<Notification> getAllNotifications(@NotNull PageRequestParam prp) throws ParseException;

    void processNotification(NotificationRequestDto notificationRequest, String module) throws Exception;
}