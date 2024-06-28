package org.core.backend.ticketapp.passport.service.core.notification;



import org.core.backend.ticketapp.passport.entity.Notification;

import java.util.UUID;

public interface INotification {

    void sendNotificationToSingleUser(UUID userId, Notification notification);
}