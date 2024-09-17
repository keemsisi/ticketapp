package org.core.backend.ticketapp.passport.service.core.messagebroker;

import org.core.backend.ticketapp.passport.dtos.NotificationRequestDto;
import org.core.backend.ticketapp.passport.entity.Notification;

public interface NotificationMessageConsumerService {
    Object processMessages(byte[] bytes, String messageId);

    void sendWebSocketNotificationToUsers(String string);

    Notification processNotification(NotificationRequestDto notificationRequestDto, String messageId) throws Exception;
}
