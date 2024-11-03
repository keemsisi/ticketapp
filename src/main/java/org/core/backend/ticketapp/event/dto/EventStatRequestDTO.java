package org.core.backend.ticketapp.event.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventStatRequestDTO {
    private UUID eventId;
    private UUID userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID tenantId;
}
