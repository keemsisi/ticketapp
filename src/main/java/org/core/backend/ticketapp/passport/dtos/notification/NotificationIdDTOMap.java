package org.core.backend.ticketapp.passport.dtos.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class NotificationIdDTOMap {
    private UUID notificationId;
    private UUID userId;
    private Integer retryCount;
}