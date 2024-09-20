package org.core.backend.ticketapp.passport.service.core.messagebroker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.NotificationProcessorStatus;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.core.backend.ticketapp.passport.dao.WorkflowDao;
import org.core.backend.ticketapp.passport.dtos.NotificationRequestDto;
import org.core.backend.ticketapp.passport.dtos.core.ApprovalLevel;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.repository.NotificationRepository;
import org.core.backend.ticketapp.passport.repository.NotificationSubscriberRepository;
import org.core.backend.ticketapp.passport.service.core.notification.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationMessageConsumerServiceImpl implements NotificationMessageConsumerService {
    private final ObjectMapper objectMapper;
    private NotificationRepository notificationRepository;
    private NotificationSubscriberRepository notificationSubscriberRepository;
    private WorkflowDao workflowRepository;
    private NotificationService notificationService;

    @Override
    public Object processMessages(byte[] bytes, String messageId) {
        return null;
    }

    public void sendWebSocketNotificationToUsers(String notificationId) {
        var notificationUUID = UUID.fromString(notificationId);
        Optional<Notification> result = notificationRepository.findByUUID(notificationUUID);
        List<UUID> userIds = new ArrayList<>();
        if (result.isPresent()) {
            var notification = result.get();
            String workFlowData = notification.getWorkflow();
            try {
                ApprovalLevel[] approvalLevels = objectMapper.readValue(workFlowData, ApprovalLevel[].class);
                Arrays.stream(approvalLevels).sequential().forEach(approvalLevel -> {
                    UUID levelApprovedBy = approvalLevel.getApprovedBy();
                    userIds.add(levelApprovedBy);
                });
                userIds.add(notification.getRequestedBy());
                var userCurrentSessionIds = notificationSubscriberRepository.findByUserIdAndNotUnsubscribedAndActive(
                        userIds, false, true);
                userCurrentSessionIds.ifPresent(notificationSubscribers -> {
                    notificationService.sendNotificationToClients(userCurrentSessionIds.get(),
                            notificationService.createMessageForProcessorUpdate(notification), true);
                });
            } catch (Exception e) {
                log.error("----||||ERROR OCCURRED WHILE PROCESSING NOTIFICATION PROCESSOR UPDATE {0}||||----", e);
            }
        }
    }

    @Override
    public Notification processNotification(final NotificationRequestDto notificationRequestDto,final String messageId) throws Exception {
        List<NotificationType> notificationTypes = List.of(
                NotificationType.IN_APP, NotificationType.REMINDER, NotificationType.RELIEF_REQUEST,
                NotificationType.APPROVAL);
        log.info("------------|||||||CONSUMING MESSAGE FROM SERVICE BUS|||||------------");
        final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        Notification notification = new Notification();

        var approvalLevels = workflowRepository.getWorkflowApprovalLevelByActionCode(notificationRequestDto.getActionName());
        if (!approvalLevels.isEmpty()) {
            notification.setWorkflow(objectWriter.writeValueAsString(approvalLevels));
            notification.setWorkflowId(approvalLevels.get(0).getWorkflowId());
        } else {
            notification.setWorkflow(null);
            notification.setWorkflowId(null);
        }
        notification.setId(UUID.randomUUID());
        notification.setModuleSubscriptionName(notificationRequestDto.getModuleSubscriptionName());
        notification.setDateCreated(LocalDateTime.now());
        notification.setModuleId(UUID.fromString(notificationRequestDto.getModuleId()));
        notification.setTenantId(UUID.fromString(notificationRequestDto.getTenantId()));
        notification.setApprovalStatus(ApprovalStatus.PENDING);
        notification.setActionName(notificationRequestDto.getActionName());
        notification.setOldData(notificationRequestDto.getOldData());
        notification.setNewData(notificationRequestDto.getNewData());
        notification.setRequestedBy(UUID.fromString(notificationRequestDto.getRequestedBy()));//prod
        notification.setDateRequested(notificationRequestDto.getDateRequested());
        notification.setMetaData(notificationRequestDto.getMetaData());
        notification.setDescription(notificationRequestDto.getDescription());
        notification.setNotificationType(notificationRequestDto.getNotificationType());
        notification.setNotificationForUserId(notificationRequestDto.getNotificationForUserId());
        notification.setMessageId(messageId);
        notification.setSubAction(notificationRequestDto.getSubAction());

        if (notificationTypes.contains(notification.getNotificationType())) {
            notification.setShouldSendInAppAlert(true);
            notification.setProcessedInAppStatusUpdate(false);
        }

        var isInApp = notificationRequestDto.getActionName().equalsIgnoreCase("in_app_notification");

        if (isInApp) {
            notification.setApprovalStatus(ApprovalStatus.APPROVED);
            notification.setDateApproved(LocalDateTime.now());
            notification.setProcessorRemark("User in-app notification processed");
            notification.setProcessorStatus(NotificationProcessorStatus.COMPLETED_SUCCESSFULLY);
            notification.setNotificationType(NotificationType.IN_APP);
            notification.setFinalApprovalByName("system");
            notification.setNotificationForUserId(notificationRequestDto.getNotificationForUserId());
        }
        notificationRepository.save(notification);
        log.info("------------|||||||MESSAGE CONSUMED SUCCESSFULLY|||||------------");
        log.info("------------|||||||SENDING NOTIFICATION TO PERMITTED SUBSCRIBERS|||||------------");
        if (isInApp) sendWebSocketNotificationToUsers(notification.getId().toString());
        else notificationService.pushNotificationToPermittedClients(notification);
        return notification;
    }
}
