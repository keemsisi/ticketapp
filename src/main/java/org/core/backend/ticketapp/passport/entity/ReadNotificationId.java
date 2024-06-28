package org.core.backend.ticketapp.passport.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ReadNotificationId implements Serializable {
    private UUID userId;
    private UUID notificationId;
}
