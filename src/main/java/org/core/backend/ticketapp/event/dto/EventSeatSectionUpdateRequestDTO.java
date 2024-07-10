package org.core.backend.ticketapp.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventSeatSectionUpdateRequestDTO(
        String name,
        Long capacity,
        double price,
        ApprovalStatus approvalStatus
) {
}
