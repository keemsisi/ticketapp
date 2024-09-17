package org.core.backend.ticketapp.passport.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.core.backend.ticketapp.passport.dao.INotificationDao;
import org.core.backend.ticketapp.passport.dao.IReliefRequestDao;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationIdDTOMap;
import org.core.backend.ticketapp.passport.entity.WebSocketPushNotification;
import org.core.backend.ticketapp.passport.repository.NotificationRepository;
import org.core.backend.ticketapp.passport.repository.WebSocketInAppNotificationRepository;
import org.core.backend.ticketapp.passport.service.core.ReminderNotificationService;
import org.core.backend.ticketapp.passport.service.core.messagebroker.NotificationMessageConsumerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SchedulerService {
    private final IReliefRequestDao iReliefRequestDao;
    private final ReminderNotificationService reminderNotificationService;
    private final NotificationRepository notificationRepository;
    private final INotificationDao iNotificationDao;
    private final WebSocketInAppNotificationRepository webSocketInAppNotificationRepository;
    private final NotificationMessageConsumerService messageConsumer;

    //    @Scheduled(cron = "0 */5 * * * *")
    @SchedulerLock(name = "account_processing", lockAtLeastFor = "4m", lockAtMostFor = "10m")
    public void execute() {
        LockAssert.assertLocked();
        process();
    }

    @Scheduled(cron = "59 59 23 * * *")
    @SchedulerLock(name = "notification_processing", lockAtLeastFor = "4m", lockAtMostFor = "10m")
    public void executeOncePerDay() {
        LockAssert.assertLocked();
        deleteAllExpiredRolesActionAndGroupsForAReliefOfficer();
    }

    @Scheduled(cron = "*/5 * * * * *")
    @SchedulerLock(name = "send_pending_in_app_notification", lockAtLeastFor = "1m", lockAtMostFor = "4m")
    public void sendPendingPushNotification() {
        LockAssert.assertLocked();
        processAllPendingPushNotifications();
    }

    @Scheduled(cron = "*/1 * * * * *")
    @SchedulerLock(name = "send_pending_web_socket_push_notifications", lockAtLeastFor = "1m", lockAtMostFor = "4m")
    public void sendPendingWebSocketPushNotifications() {
        LockAssert.assertLocked();
        processUnDeliveredWebSocketPushNotifications();
    }

    @Scheduled(cron = "*/1 * * * * *")
    @SchedulerLock(name = "reminder_notification_processing", lockAtLeastFor = "2s", lockAtMostFor = "5s")
    public void executeEverySec() {
        LockAssert.assertLocked();
        sendReminderNotifications();
    }

    private boolean process() {
        try {
            return true;
        } catch (Exception e) {
            log.error("Exception processing loan request", e);
            return false;
        }
    }

    void deleteAllExpiredRolesActionAndGroupsForAReliefOfficer() {
        try {
            log.info("----||||DELETING ALL EXPIRED PERMISSIONS----||||");
            iReliefRequestDao.deleteAllExpiredActionsGroupsAndActions();
        } catch (Exception e) {
            log.error("Error : ", e);
        }
    }

    void sendReminderNotifications() {
        reminderNotificationService.sendEmailAndInAppReminderNotification();
    }


    private void processAllPendingPushNotifications() {
        List<NotificationIdDTOMap> pendingInAppNotifications = iNotificationDao.getAllPendingPushNotificationProcessorStatusUpdate();
        if (pendingInAppNotifications.size() > 0) {
            log.info("---|||PENDING UN-PROCESSED {} IN_APP NOTIFICATIONS|||---", pendingInAppNotifications.size());
            pendingInAppNotifications.stream().parallel().forEach(dtoMap -> {
                messageConsumer.sendWebSocketNotificationToUsers(dtoMap.getNotificationId().toString());
                notificationRepository.updateProcessedPushNotificationStatusUpdate(dtoMap.getNotificationId());
            });
        }
    }

    private void processUnDeliveredWebSocketPushNotifications() {
        List<WebSocketPushNotification> webSocketPushNotifications = iNotificationDao.getUnDeliveredWebSocketPushNotifications();
        if (webSocketPushNotifications.size() > 0) {
            log.info("---|||UNDELIVERED {} IN_APP NOTIFICATIONS|||---", webSocketPushNotifications.size());
//            webSocketPushNotifications.stream().parallel().forEach(webSocketInAppNotification ->
//                    messageConsumer.sendWebSocketNotificationToUsers(webSocketInAppNotification.getNotificationId().toString()));
        }
    }
}
