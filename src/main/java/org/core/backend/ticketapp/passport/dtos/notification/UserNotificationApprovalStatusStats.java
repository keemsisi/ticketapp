package org.core.backend.ticketapp.passport.dtos.notification;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationApprovalStatusStats {
    private Long approved;
    private Long rejected;
    private Long pending;
    private Long declined;
    private Long sent;
}
