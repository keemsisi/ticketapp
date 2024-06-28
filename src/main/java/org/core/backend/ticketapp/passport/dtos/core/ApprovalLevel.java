package org.core.backend.ticketapp.passport.dtos.core;

import lombok.*;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLevel {

    private UUID workflowId;

    private UUID levelId;

    private UUID roleId;

    private UUID approvedBy;

    private Integer levelNo;

    private String roleCode;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    private String remarks;

    private LocalDateTime approvalDate;

}
