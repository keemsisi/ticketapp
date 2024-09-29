package org.core.backend.ticketapp.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.enums.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventStatRequestDTO {
    private UUID eventId;
    private UUID userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID tenantId;
}
