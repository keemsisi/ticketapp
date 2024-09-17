package org.core.backend.ticketapp.passport.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.NotificationType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private String id;
    @NotNull
    private String moduleId;//A_E
    @NotNull
    private String requestedBy;//A_E
    @NotNull
    private String newData;
    private String oldData;
    private String metaData; // json string
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @NotNull
    @NotBlank
    private String actionName;
    private LocalDateTime dateCreated;
    private LocalDateTime dateApproved;
    private String declineReason;
    private LocalDateTime dateDeclined;
    @NotNull
    private LocalDateTime dateRequested;
    private String workflow;
    //    @NotNull
    private String moduleSubscriptionName;
    private String description;
    private UUID notificationForUserId;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // RELIEF_REQUEST, SUBSCRIPTION
    private String subAction;
    private String tenantId;
}
