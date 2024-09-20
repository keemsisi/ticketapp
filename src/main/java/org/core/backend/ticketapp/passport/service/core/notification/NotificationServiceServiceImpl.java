package org.core.backend.ticketapp.passport.service.core.notification;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.ApplicationContextProvider;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.NotificationProcessorStatus;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.service.VirtualEventService;
import org.core.backend.ticketapp.passport.dao.NotificationServiceDao;
import org.core.backend.ticketapp.passport.dtos.NotificationRequestDto;
import org.core.backend.ticketapp.passport.dtos.PageRequestParam;
import org.core.backend.ticketapp.passport.dtos.core.ApprovalLevel;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.notification.BulkRequestApprovalDto;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationReadDTO;
import org.core.backend.ticketapp.passport.dtos.notification.SingleRequestApprovalDto;
import org.core.backend.ticketapp.passport.dtos.notification.UserNotificationApprovalStatusStats;
import org.core.backend.ticketapp.passport.entity.*;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationModuleStatsWrapper;
import org.core.backend.ticketapp.passport.mapper.UserUnreadNotificationStatsByModuleId;
import org.core.backend.ticketapp.passport.repository.NotificationRepository;
import org.core.backend.ticketapp.passport.repository.NotificationSubscriberRepository;
import org.core.backend.ticketapp.passport.repository.ReadNotificationRepository;
import org.core.backend.ticketapp.passport.repository.WebSocketInAppNotificationRepository;
import org.core.backend.ticketapp.passport.service.core.BaseRepoService;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.service.core.messagebroker.NotificationMessageConsumerServiceImpl;
import org.core.backend.ticketapp.passport.socketio.dto.Message;
import org.core.backend.ticketapp.passport.socketio.dto.MessageDto;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.CommonUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceServiceImpl extends BaseRepoService<Notification> implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final NotificationSubscriberRepository notificationSubscriberRepository;
    private final NotificationServiceDao notificationServiceDao;
    private final ReadNotificationRepository readNotificationRepository;
    private final SocketIOServer socketIOServer;
    private final ActivityLogProcessorUtils activityLogProcessorUtils;
    private final WebSocketInAppNotificationRepository webSocketInAppNotificationRepository;
    private final VirtualEventService virtualEventService;
    private final EventRepository eventRepository;
    private final CoreUserService coreUserService;

    @Override
    public Page<Notification> getAllByActionNameStatusAndModuleIdPaged(String actionName, ApprovalStatus status, UUID moduleId, Date startDate, Date endDate, Pageable pageable) {
        UUID tenantId = jwtTokenUtil.getUser().getTenantId();
        if (ObjectUtils.isNotEmpty(actionName) && ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionNameAndStatusAndStartDateAndEndDatePaged(actionName, status.name(), startDate, endDate, tenantId, pageable);
            return notificationRepository.getAllByActionNameAndStatusAndModuleIdAndStartDateAndEndDatePaged(actionName, status.name(), moduleId, startDate, endDate, tenantId, pageable);
        } else if (ObjectUtils.isNotEmpty(actionName) && ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionNameAndStartDateAndEndDatePaged(actionName, startDate, endDate, tenantId, pageable);
            return notificationRepository.getAllByActionNameAndModuleIdAndStartDateAndEndDatePaged(actionName, moduleId, startDate, endDate, tenantId, pageable);
        } else if (ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllStatusAndStartDateAndEndDatePaged(status.name(), startDate, endDate, tenantId, pageable);
            return notificationRepository.getAllStatusAndModuleIdAndStartDateAndEndDatePaged(status.name(), moduleId, startDate, endDate, tenantId, pageable);
        } else if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllStartDateAndEndDatePaged(startDate, endDate, tenantId, NotificationType.APPROVAL.name(), pageable);
            return notificationRepository.getAllModuleIdAndStartDateAndEndDatePaged(moduleId, startDate, endDate, tenantId, pageable);
        } else if (ObjectUtils.isNotEmpty(actionName) && ObjectUtils.isNotEmpty(status)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionNameAndStatusPaged(actionName, status.name(), tenantId, pageable);
            return notificationRepository.getAllByActionNameAndStatusAndModuleIdPaged(actionName, status.name(), moduleId, tenantId, pageable);
        } else if (ObjectUtils.isNotEmpty(actionName)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionName(actionName, tenantId, pageable);
            return notificationRepository.getAllByActionNameAndModuleId(actionName, moduleId, tenantId, pageable);
        } else if (status != null) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByStatus(status.name(), tenantId, NotificationType.APPROVAL.name(), pageable);
            return notificationRepository.getAllByStatusAndModuleId(status.name(), moduleId, tenantId, pageable);
        } else {
            if (ObjectUtils.isEmpty(moduleId))
                if (jwtTokenUtil.getUser().getRoles().contains("super_admin"))
                    return notificationRepository.getAllByTenantIdPaged(tenantId, pageable);
                else
                    throw new ApplicationException(403, "Forbidden", "Oops! Only a super admin can get all notifications by tenant");
            return notificationRepository.getAllByModuleId(moduleId, tenantId, pageable);
        }
    }

    @Override
    public List<Notification> getAllByActionNameStatusAndModuleIdUnPaged(String actionName, ApprovalStatus status, Date startDate, Date endDate, UUID moduleId) {
        UUID tenantId = jwtTokenUtil.getUser().getTenantId();
        if (ObjectUtils.isNotEmpty(actionName) && ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionNameAndStatusAndStartDateAndEndDateUnPaged(actionName, status.name(), startDate, endDate, tenantId);
            return notificationRepository.getAllByActionNameAndStatusAndModuleIdAndStartDateAndEndDateUnPaged(actionName, status.name(), moduleId, startDate, endDate, tenantId);
        } else if (ObjectUtils.isNotEmpty(actionName) && ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionNameAndStartDateAndEndDateUnPaged(actionName, startDate, endDate, tenantId);
            return notificationRepository.getAllByActionNameAndModuleIdAndStartDateAndEndDateUnPaged(actionName, moduleId, startDate, endDate, tenantId);
        } else if (ObjectUtils.isNotEmpty(status) && ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByStatusAndStartDateAndEndDateUnPaged(status.name(), startDate, endDate, tenantId);
            return notificationRepository.getAllByStatusAndModuleIdAndStartDateAndEndDateUnPaged(status.name(), moduleId, startDate, endDate, tenantId);
        } else if (ObjectUtils.isNotEmpty(startDate) && ObjectUtils.isNotEmpty(endDate)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllStartDateAndEndDateUnPaged(startDate, endDate, tenantId, NotificationType.APPROVAL.name());
            return notificationRepository.getAllModuleIdAndStartDateAndEndDateUnPaged(moduleId, startDate, endDate, tenantId);
        } else if (ObjectUtils.isNotEmpty(actionName) && ObjectUtils.isNotEmpty(status)) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByActionNameAndStatus(actionName, status.name(), tenantId);
            return notificationRepository.getAllByActionNameAndStatusAndModuleId(actionName, status.name(), moduleId, tenantId);
        } else if (ObjectUtils.isNotEmpty(actionName)) {
            if (ObjectUtils.isEmpty(moduleId)) return notificationRepository.getAllByActionName(actionName, tenantId);
            return notificationRepository.getAllByActionNameAndModuleId(actionName, moduleId, tenantId);
        } else if (status != null) {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByStatus(status.name(), tenantId, NotificationType.APPROVAL.name());
            return notificationRepository.getAllByStatusAndModuleId(status.name(), moduleId, tenantId);
        } else {
            if (ObjectUtils.isEmpty(moduleId))
                return notificationRepository.getAllByTenantId(tenantId);
            return notificationRepository.getAllByModuleId(moduleId, tenantId);
        }
    }

    @Override
    public void sendNotificationToClients(List<NotificationSubscriber> usersCurrentSessionIds, Message message, boolean inApp) {
        usersCurrentSessionIds.parallelStream().forEach(ns -> {
            int RETRIES_MAX_THRESHOLD = 6, count = 0;
            boolean messageSent = false;
            var sessionUUID = ns.getCurrentSessionId();
            while (count < RETRIES_MAX_THRESHOLD) {
                if (messageSent) break;
                if (count == 0) ++count;
                else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        log.error("Error Occurred ::: ->", e);
                    }
                }
                ++count;
                SocketIOClient socketIOClient = socketIOServer.getClient(sessionUUID);
                if (ObjectUtils.isNotEmpty(socketIOClient)) {
                    socketIOClient.sendEvent("new_notification", message);
                    log.info("------||||SENT NOTIFICATION TO SESSION_ID {}||||------", sessionUUID);
                    messageSent = true;
                } else messageSent = false;
            }
            if (!messageSent)
                log.info("-----|||| FAILED TO RETRIEVE CLIENT WITH SESSION_ID {} FROM REDIS", sessionUUID);
            if (inApp) createInAppNotificationForUser(message.getData(), ns, messageSent);
        });
    }

    private void createInAppNotificationForUser(MessageDto data, NotificationSubscriber notificationSubscriber, boolean messageSent) {
        var result = webSocketInAppNotificationRepository.findByNotificationIdAndUserId(
                data.getId(), notificationSubscriber.getUserId()
        );
        result.ifPresent(webSocketInAppNotification -> {
            int retryCount = webSocketInAppNotification.getRetryCount();
            if (messageSent) {
                webSocketInAppNotification.setRetryCount(retryCount == 0 ? 0 : retryCount + 1);
                webSocketInAppNotification.setNotificationDelivered(true);
                webSocketInAppNotification.setDateDelivered(LocalDateTime.now());
                log.info("---||||[messageSent->true]SAVED SUCCESSFUL IN_APP PUB NOTIFICATION||||---");
            } else {
                webSocketInAppNotification.setRetryCount(retryCount + 1);
                webSocketInAppNotification.setNotificationDelivered(false);
                webSocketInAppNotification.setDateLastSent(LocalDateTime.now());
                log.info("---||||[messageSent->false]SAVED FAILED IN_APP PUB NOTIFICATION||||---");
            }
            webSocketInAppNotification.setSessionId(notificationSubscriber.getCurrentSessionId());
            webSocketInAppNotificationRepository.save(webSocketInAppNotification);
        });
        if (result.isEmpty()) {
            final var webSocketInAppNotification = WebSocketPushNotification.builder()
                    .notificationId(data.getId()).dateCreated(LocalDateTime.now()).notificationDelivered(messageSent)
                    .dateDelivered(messageSent ? LocalDateTime.now() : null)
                    .retryCount(0).sessionId(notificationSubscriber.getCurrentSessionId())
                    .userId(notificationSubscriber.getUserId()).dateLastSent(LocalDateTime.now())
                    .tenantId(notificationSubscriber.getTenantId()).build();
            webSocketInAppNotificationRepository.save(webSocketInAppNotification);
            log.info("---||||CREATED NEW ENTRY FOR FAILED IN_APP PUB NOTIFICATION||||---");
        }

    }

    @Override
    public Message createMessage(final Notification notification) {
        Message message = new Message();
        var messageDto = MessageDto.builder()
                .id(notification.getId())
                .createdBy(notification.getRequestedBy())
                .requestedByName(notification.getRequestedByName())
                .actionName(notification.getActionName())
                .dateCreated(Timestamp.valueOf(notification.getDateCreated()))
                .moduleId(notification.getModuleId())
                .dateReceived(new Date())
                .status(notification.getApprovalStatus())
                .notificationType(notification.getNotificationType())
                .tenantId(notification.getTenantId())
                .title(notification.getTitle())
                .processorStatus(notification.getProcessorStatus())
                .processorRemark(notification.getProcessorRemark())
                .description(notification.getDescription()).build();
        message.setMessage(String.format("New %s notification.", notification.getNotificationType().name().toLowerCase()));
        message.setData(messageDto);
        return message;
    }

    @Override
    public Message createMessageForProcessorUpdate(final Notification notification) {
        var message = createMessage(notification);
        message.getData().setNotificationType(NotificationType.IN_APP);
        message.getData().setDescription(String.format("New notification processor status received after final approval\nMessage : %s",
                message.getData().getProcessorRemark()));
        return message;
    }

    @Override
    public void sendNotificationToSingleUser(UUID userId, Notification notification) {
        Optional<NotificationSubscriber> ns = notificationSubscriberRepository.findByUserIdAndNotUnsubscribedAndActive(userId, false, true);
        ns.ifPresent(nsFound -> sendNotificationToClients(List.of(nsFound), createMessage(notification), false));
    }

    @Override
    public void pushNotificationToPermittedClients(Notification notification) {
        List<NotificationSubscriber> notificationSubscribers = getSubscribersCurrentSessionIds(notification.getActionName(), notification.getTenantId());
        if (notificationSubscribers.size() > 0) {
            log.info("----||||FOUND USER(s) TO SEND NOTIFICATION TO||||----");
            sendNotificationToClients(notificationSubscribers, createMessage(notification), false);
        } else log.info("----||||NO MATCHED USER FOUND TO SEND NOTIFICATION TO||||----");
    }

    private List<NotificationSubscriber> getSubscribersCurrentSessionIds(String actionName, UUID tenantId) {
        return notificationServiceDao.getAllMatchingSubscriberCurrentSessionIds(actionName, tenantId);
    }

    @Override
    public Object getUserNotificationsByUserIdTenantId(Pageable pageable, Sort.Direction direction) {
        final var loggedInUserDto = jwtTokenUtil.getUser();
        if (pageable == null) {
            return notificationRepository.getAllUserUnreadNotificationsUnPaged(loggedInUserDto.getUserId(), loggedInUserDto.getScope(), loggedInUserDto.getTenantId());
        } else {
            return notificationRepository.getAllUserUnreadNotificationsPaged(loggedInUserDto.getUserId(), loggedInUserDto.getScope(), loggedInUserDto.getTenantId(), pageable);
        }
    }

    @Override
    public void readNotification(@NotNull NotificationReadDTO notificationReadDTO) {
        /*TODO: Check if the user has access to read the notification*/
        LoggedInUserDto loggedInUserDto = jwtTokenUtil.getUser();
        notificationReadDTO.getNotificationIds().forEach(uuid -> {
            try {
                if (loggedInUserDto != null) {
                    Optional<Notification> optionalNotification = notificationRepository.findById(uuid);
                    if (optionalNotification.isPresent()) {
                        Notification notification = optionalNotification.get();
                        ReadNotificationId readNotificationId = new ReadNotificationId(loggedInUserDto.getUserId(), notification.getId());
                        ReadNotification readNotification = new ReadNotification();
                        readNotification.setDateCreated(LocalDateTime.now());
                        readNotification.setReadNotificationId(readNotificationId);
                        readNotificationRepository.save(readNotification);
                        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Notification.class.getTypeName(), null, objectMapper.writeValueAsString(readNotification), "You read a notification");
                    } else {
                        throw new ApplicationException(404, "notification_not_found", String.format("notification can't be marked as read for user[%s]", loggedInUserDto.getUserId()));
                    }
                } else {
                    throw new ApplicationException(403, "user_unauthorised", "User not found");
                }
            } catch (Exception e) {
                log.error("", e);
                throw new ApplicationException(400, "read_receipt_failed", String.format("failed to mark notification[%s] as read", loggedInUserDto.getUserId()));
            }
        });
    }

    @Override
    public Notification getNotificationById(final UUID id) {
        Optional<Notification> optionalNotification = notificationRepository.findByUUID(id);
        if (optionalNotification.isPresent()) {
            return optionalNotification.get();
        }
        throw new ApplicationException(404, "notification_not_found", String.format("notification with id[%s] not found", id));
    }

    @Override
    @SneakyThrows({JsonProcessingException.class})
    public void approveRequest(SingleRequestApprovalDto singleRequestApprovalDto) throws Exception {
        Optional<Notification> optionalNotification = notificationRepository.findByUUID(UUID.fromString(singleRequestApprovalDto.getNotificationId()));
        if (optionalNotification.isPresent()) {
            LoggedInUserDto loggedInUserDto = jwtTokenUtil.getUser();
            Notification notification = optionalNotification.get();
            String oldDataJSON = objectMapper.writeValueAsString(notification);
            if (notification.getApprovalStatus().equals(ApprovalStatus.PENDING)) {
                if (notification.getNotificationType() == NotificationType.RELIEF_REQUEST) {
                    if (notification.getNotificationForUserId().equals(loggedInUserDto.getUserId())) {
                        notification.setFinalApprovalByName(CommonUtils.getFullName(loggedInUserDto));
                        approveAndPublishNotificationWithoutWorkflow(notification, singleRequestApprovalDto, loggedInUserDto);
                        //send mail to the user who created request
//                        reliefRequestService.sendMailToReliefOfficer();
                        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Notification.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(notification), singleRequestApprovalDto.getApprovalStatus().name() + " a leave request notification");
                    } else
                        throw new ApplicationException(403, "Forbidden", "Oops! This relief request notification is not for you and approval is forbidden");
                } else if (notification.getNotificationType() == NotificationType.APPROVAL) {
                    validaUserAccessToNotification(notification, loggedInUserDto);
                    if (notification.getWorkflow() == null) {
                        notification.setFinalApprovalByName(CommonUtils.getFullName(loggedInUserDto));
                        approveAndPublishNotificationWithoutWorkflow(notification, singleRequestApprovalDto, loggedInUserDto);
                        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Notification.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(notification), singleRequestApprovalDto.getApprovalStatus().name() + " a subscription notification without workflow");
                    } else {
                        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS"));
                        final var approvalLevels = Arrays.asList(objectMapper.readValue(notification.getWorkflow(), ApprovalLevel[].class));
                        approvalLevels.sort(Comparator.comparing(ApprovalLevel::getLevelNo));
                        for (int i = 0; i < approvalLevels.size(); i++) {
                            var level = approvalLevels.get(i);
                            if (level.getApprovalStatus() == ApprovalStatus.PENDING) {
                                if (loggedInUserDto.getRoles().contains(level.getRoleCode())) {
                                    level.setApprovalStatus(singleRequestApprovalDto.getApprovalStatus());
                                    level.setApprovalDate(LocalDateTime.now());
                                    level.setApprovedBy(loggedInUserDto.getUserId());
                                    level.setRemarks(singleRequestApprovalDto.getRemark());
                                    notification.setWorkflow(objectMapper.writeValueAsString(approvalLevels));
                                    if (singleRequestApprovalDto.getApprovalStatus() != ApprovalStatus.APPROVED) {
                                        //TODO: Do we still need to published all unapproved requests
                                        notification.setFinalApprovalByName(CommonUtils.getFullName(loggedInUserDto));
                                        completeApprovalAndPublish(notification, singleRequestApprovalDto);
                                        break;
                                    }
                                    if (i + 1 != approvalLevels.size())
                                        notification.setProcessorRemark(String.format("Workflow level approval has now moved to level (%s/%s)", i + 2, approvalLevels.size()));
                                    if (i + 2 == approvalLevels.size())
                                        notification.setProcessorRemark(String.format("Waiting for final workflow level approval (%s/%s) to be completed...", approvalLevels.size(), approvalLevels.size()));
                                    if (i == approvalLevels.size() - 1) {
                                        notification.setProcessorRemark(String.format("All workflow level approvals (%s/%s) completed successfully...", i + 1, approvalLevels.size()));
                                        notification.setFinalApprovalByName(CommonUtils.getFullName(loggedInUserDto));
                                        completeApprovalAndPublish(notification, singleRequestApprovalDto);
                                    }
                                    notificationRepository.save(notification);
                                    activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Notification.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(notification), singleRequestApprovalDto.getApprovalStatus().name() + " a notification with workflow");
                                    break;
                                } else {
                                    throw new ApplicationException(403, "Forbidden", String.format("Oops! You don't the right authority (%s) to approve for the current level %s ", level.getRoleCode(), level.getLevelNo()));
                                }
                            }
                        }
                    }
                }
            } else if (notification.getApprovalStatus() == ApprovalStatus.APPROVED && notification.getProcessorStatus() == NotificationProcessorStatus.COMPLETED_WITH_ERROR) {
                notification.setDateApproved(null);
                notification.setDateModified(LocalDateTime.now());
                notification.setFinalApprovalByName(CommonUtils.getFullName(loggedInUserDto));
                notification.setProcessorRemark("PENDING -> Reprocessing Failed Notification!");
                completeApprovalAndPublish(notification, singleRequestApprovalDto);
            } else
                throw new ApplicationException(409, "conflict", String.format("Request with id (%s) has already been processed with approvalStatus (%s)", singleRequestApprovalDto.getNotificationId(), notification.getApprovalStatus().name()));
        } else
            throw new ApplicationException(400, "bad_request", String.format("Notification  with the given ID %s was not found ", singleRequestApprovalDto.getNotificationId()));
    }

    private void validaUserAccessToNotification(Notification notification, LoggedInUserDto loggedInUserDto) {
        if (ObjectUtils.notEqual(notification.getTenantId(), loggedInUserDto.getTenantId()))
            throw new ApplicationException(403, "Forbidden", String.format("You are not part of this tenant id(%s)", notification.getTenantId()));
        if (!loggedInUserDto.getScope().contains(notification.getActionName().toLowerCase()))
            throw new ApplicationException(403, "Forbidden", String.format("Oops! You don't have authority to perform approval on this notification(%s)", notification.getActionName()));
    }

    @Transactional
    @SneakyThrows({JsonProcessingException.class})
    public void approveAndPublishNotificationWithoutWorkflow(Notification notification, SingleRequestApprovalDto
            approval, LoggedInUserDto userDto) throws Exception {
        var approvalLevel = ApprovalLevel.builder()
                .approvalDate(LocalDateTime.now())
                .approvalStatus(approval.getApprovalStatus())
                .remarks(approval.getRemark())
                .approvedBy(userDto.getUserId())
                .build();
        notification.setWorkflow(objectMapper.writeValueAsString(List.of(approvalLevel)));
        notification.setApprovalStatus(approval.getApprovalStatus());
        notification.setDateApproved(LocalDateTime.now());
        notificationRepository.save(notification);
        processApprovedNotification(notification);
        log.info("----||||Publishing Request with no workflow----||||");
    }

    private void completeApprovalAndPublish(final Notification notification, final SingleRequestApprovalDto approval) throws Exception {
        notification.setApprovalStatus(approval.getApprovalStatus());
        notification.setDateApproved(LocalDateTime.now());
        notificationRepository.save(notification);
        processApprovedNotification(notification);
        log.info("----||||Publishing Request with no workflow----||||");
    }

    private void completeApprovalWithoutPublish(Notification notification, SingleRequestApprovalDto approval) {
        notification.setApprovalStatus(approval.getApprovalStatus());
        notification.setDateApproved(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    public void notificationBulkApproval(BulkRequestApprovalDto bulkRequestApprovalDto) throws
            Exception {
        SingleRequestApprovalDto singleRequestApprovalDto = new SingleRequestApprovalDto();
        if (validateActionNames(getActionNamesByNotificationIds(bulkRequestApprovalDto.getNotificationId()))) {
            for (UUID uuid : bulkRequestApprovalDto.getNotificationId()) {
                singleRequestApprovalDto.setApprovalStatus(bulkRequestApprovalDto.getApprovalStatus());
                singleRequestApprovalDto.setRemark(bulkRequestApprovalDto.getRemark());
                singleRequestApprovalDto.setNotificationId(uuid.toString());
                approveRequest(singleRequestApprovalDto);
            }
            return;
        }
        throw new ApplicationException(400, "Bad Request", String.format("action names for the notification ids %s has to be the same.", bulkRequestApprovalDto.getNotificationId().toString()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserNotificationApprovalStatusStats getUserNotificationApprovalStatusStats() {
        LoggedInUserDto loggedInUserDto = jwtTokenUtil.getUser();
        Map<String, Object> results = notificationServiceDao.getUserNotificationStats(loggedInUserDto.getScope(), loggedInUserDto.getRoles(), loggedInUserDto.getUserId(), loggedInUserDto.getTenantId());
        return UserNotificationApprovalStatusStats
                .builder()
                .sent(((ArrayList<LongWrapper>) results.get("sent")).get(0).getCount())
                .approved(((ArrayList<LongWrapper>) results.get("approved")).get(0).getCount())
                .declined(((ArrayList<LongWrapper>) results.get("declined")).get(0).getCount())
                .rejected(((ArrayList<LongWrapper>) results.get("rejected")).get(0).getCount())
                .pending(((ArrayList<LongWrapper>) results.get("pending_by_role_code")).get(0).getCount() + ((ArrayList<LongWrapper>) results.get("pending_by_action_name")).get(0).getCount())
                .build();
    }

    @Override
    public List<UserNotificationGroupByDateCreatedWrapper> fetchUserNotificationsReceivedByDateRange(Date startDate, Date endDate) {
        return notificationServiceDao.getUserNotificationsReceivedByDateRange(jwtTokenUtil.getUser().getScope(), startDate, endDate, jwtTokenUtil.getUser().getTenantId());
    }

    @Override
    public List<UserNotificationModuleStatsWrapper> getUserModuleStats() {
        return notificationServiceDao.getUserModuleStats(jwtTokenUtil.getUser().getScope(), jwtTokenUtil.getUser().getTenantId());
    }

    @Override
    public List<UserUnreadNotificationStatsByModuleId> getUserUnreadNotificationsStatsByModuleId(UUID moduleId) {
        return notificationServiceDao.getUserNotificationStatsByModuleId(jwtTokenUtil.getUser().getUserId(), jwtTokenUtil.getUser().getScope(), jwtTokenUtil.getUser().getTenantId(), moduleId);
    }

    @Override
    public List<Notification> getAllNotifications(@NotNull PageRequestParam prp) throws ParseException {
        return notificationServiceDao.getAllNotifications(jwtTokenUtil.getUser().getUserId(), jwtTokenUtil.getUser().getRoles(),
                jwtTokenUtil.getUser().getTenantId(), prp);
    }

    @Override
    public void processNotification(final NotificationRequestDto notificationRequest, final String module) throws Exception {
        getNotificationConsumerService().processNotification(notificationRequest, module);
    }

    private boolean validateActionNames(List<String> notificationsActionNames) {
        if (notificationsActionNames.isEmpty())
            throw new ApplicationException(400, "notifications_not_found", "No matching notification found for the Ids provided");
        Set<String> actionSets = new HashSet<>(notificationsActionNames);
        return actionSets.size() == 1;
    }

    private List<String> getActionNamesByNotificationIds(@NotNull List<UUID> notificationIds) {
        return notificationRepository.getActionNamesByNotificationIds(notificationIds, jwtTokenUtil.getUser().getTenantId());
    }


    //    @Async
    private void processApprovedNotification(final Notification notification) throws Exception {
        //TODO: This can be later moved to pub/sub
        // Once notification is approved there should be a pub/sub that will push
        // the message to each service that create the notification
        switch (notification.getNotificationType()) {
            case APPROVAL -> {
                switch (notification.getMessageId()) {
                    case "event": {
                        final var mappedEvent = objectMapper.readValue(notification.getNewData(), Event.class);
                        final var event = eventRepository.getById(mappedEvent.getId());
                        final var user = coreUserService.getUserById(mappedEvent.getUserId())
                                .orElseThrow(() -> new ApplicationException(404, "not_found", "User not found!"));
                        event.setApprovalStatus(notification.getApprovalStatus());
                        notification.setProcessorRemark("Event was successful approved!");
                        if (event.getApprovalStatus() == ApprovalStatus.APPROVED) {
                            final var ownerEmail = user.getEmail();
                            if (!event.isPhysicalEvent() && StringUtils.isBlank(event.getLink())) {
                                final var eventLink = virtualEventService.createLinkWithCalendar(event, ownerEmail);
                                event.setLink(eventLink);
                                notification.setProcessorRemark("Event was successful approved and meeting link created!!");
                                log.info(">>> Created meeting link for virtual event {} with owner: {} ", eventLink, ownerEmail);
                            }
                            eventRepository.save(event);
                            notification.setProcessorStatus(NotificationProcessorStatus.COMPLETED_SUCCESSFULLY);
                            notificationRepository.save(notification);
                            log.info(">>> Successfully updated event with status: {} ", notification.getApprovalStatus());
                        } else {
                            //do something else here!
                        }
                        //send notification to the owner of the event created about the status!
                    }
                    default: {
                        //do nothing!
                    }
                }
            }
            case IN_APP -> {
                //do nothing!
            }
        }
    }

    private NotificationMessageConsumerServiceImpl getNotificationConsumerService() {
        return ApplicationContextProvider.getBean(NotificationMessageConsumerServiceImpl.class);
    }
}