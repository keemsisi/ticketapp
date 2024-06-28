package org.core.backend.ticketapp.passport.dtos.notification;

import lombok.Data;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class SingleRequestApprovalDto {
    @NotNull
    private String remark;
    @NotNull
    private ApprovalStatus approvalStatus;
    @NotNull
    @NotEmpty
    @NotBlank
    private String notificationId;
}