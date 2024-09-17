package org.core.backend.ticketapp.passport.dao;


import org.core.backend.ticketapp.passport.dtos.PageRequestParam;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationIdDTOMap;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;
import org.core.backend.ticketapp.passport.entity.WebSocketPushNotification;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationModuleStatsWrapper;
import org.core.backend.ticketapp.passport.mapper.UserUnreadNotificationStatsByModuleId;
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
    List<NotificationSubscriber> getAllMatchingSubscriberCurrentSessionIds(String actionName, UUID tenantId);
    Map<String, Object> getUserNotificationStats(List<String> actionNames, List<String> roles, UUID userId, UUID tenantId);

    List<UserNotificationModuleStatsWrapper> getUserModuleStats(List<String> actionNames, UUID tenantId);

    List<UserNotificationGroupByDateCreatedWrapper> getUserNotificationsReceivedByDateRange(List<String> actionNames, Date startDate, Date endDate, UUID tenantId);

    List<UserUnreadNotificationStatsByModuleId> getUserNotificationStatsByModuleId(UUID userId, List<String> actionNames, UUID tenantId, UUID moduleId);

    int updateNotificationProcessor(UUID notificationId, String remark, String status, LocalDateTime startDate, LocalDateTime endDate);

    @SuppressWarnings("unchecked")
    List<Notification> getAllNotifications(UUID userId, List<String> userRoles, UUID tenantId, PageRequestParam prp) throws ParseException;

    List<NotificationIdDTOMap> getAllPendingPushNotificationProcessorStatusUpdate();

    List<WebSocketPushNotification> getUnDeliveredWebSocketPushNotifications();
}
