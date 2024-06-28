package org.core.backend.ticketapp.passport.dtos.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReliefRequestDTO {
    @NotNull
    private UUID reliefOfficerId;
    private UUID requestedById;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    private String remark;
    private UUID tenantId;
    @Enumerated(value = EnumType.STRING)
    private ApprovalStatus approvalStatus;
}
