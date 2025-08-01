package org.core.backend.ticketapp.passport.dao;


import org.core.backend.ticketapp.passport.dtos.PageRequestParam;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationIdDTOMap;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;
import org.core.backend.ticketapp.passport.entity.WebSocketPushNotification;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface INotificationDao {
    List<NotificationSubscriber> getAllMatchingSubscriberCurrentSessionIds(String actionName);

    Map<String, Object> getUserNotificationStats(List<String> actionNames, List<String> roles, UUID userId);

    List<UserNotificationGroupByDateCreatedWrapper> getUserNotificationsReceivedByDateRange(List<String> actionNames, Date startDate, Date endDate);

    Page<Notification> getUserUnreadNotificationPaged(UUID userId, Pageable pageable, Sort.Direction direction);

    List<Notification> getUserUnreadNotificationUnPaged(UUID userId);

    int updateNotificationProcessor(UUID notificationId, String remark, String status, LocalDateTime startDate, LocalDateTime endDate);

    List<Notification> getAllNotifications(UUID userId, List<String> scope, PageRequestParam prp) throws ParseException;

    List<NotificationIdDTOMap> getAllPendingPushNotificationProcessorStatusUpdate();

    List<WebSocketPushNotification> getUnDeliveredWebSocketPushNotifications();
}
