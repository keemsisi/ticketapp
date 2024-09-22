package org.core.backend.ticketapp.common.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventStatsResponseDTO {
    private EventStatsDTO orderStats;
    private EventStatsDTO transactionStats;
}
