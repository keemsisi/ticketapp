package org.core.backend.ticketapp.passport.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManualNotificationDTO {
    private String data;
    private String messageId;
}
