package org.core.backend.ticketapp.common.response.stats;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventStatsResponseDTO {
    private EventStatsDTO orderStats;
    private EventStatsDTO transactionStats;
    private List<EventTransactionDateStatsDTO> transactionDateStats;
}
