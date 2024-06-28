package org.core.backend.ticketapp.passport.dtos.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConsumptionRequestDto {
    private String notificationId;
    private String status;
    private String remark;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
