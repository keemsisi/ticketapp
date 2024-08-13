package org.core.backend.ticketapp.common.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventStatsDTO {
    private long totalCapacity;
    private long totalTicketSections;
    private long totalAcquiredTickets;
    private long totalAvailableTickets;
}