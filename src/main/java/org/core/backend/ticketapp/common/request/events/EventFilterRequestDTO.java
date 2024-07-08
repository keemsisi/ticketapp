package org.core.backend.ticketapp.common.request.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.request.AbstractFilterRequestDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class EventFilterRequestDTO extends AbstractFilterRequestDTO {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateApproved;
    private ApprovalStatus approvalStatus;
}
