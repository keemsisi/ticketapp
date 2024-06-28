package org.core.backend.ticketapp.passport.dtos.notification;

import lombok.Data;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class BulkRequestApprovalDto {
    @NotNull
    private String remark;
    @NotNull
    private ApprovalStatus approvalStatus;
    @NotNull
    @NotEmpty
    @NotBlank
    private List<UUID> notificationId;
}