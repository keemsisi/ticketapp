package org.core.backend.ticketapp.common.response.stats;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventTicketStatsDTO {
    private Long totalCapacity;
    private Long totalTicketSections;
    private Long totalAcquiredTickets;
    private Long totalAvailableTickets;
}
