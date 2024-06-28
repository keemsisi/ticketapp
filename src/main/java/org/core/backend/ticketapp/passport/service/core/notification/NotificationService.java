package org.core.backend.ticketapp.passport.service.core.notification;


import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.service.core.BaseRepoService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class NotificationService extends BaseRepoService<Notification> implements INotification {

    @Override
    public void sendNotificationToSingleUser(UUID userId, Notification notification) {

    }
}