package org.core.backend.ticketapp.common.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventStatsDTO {
    private Long totalCapacity;
    private Long totalTicketSections;
    private Long totalAcquiredTickets;
    private Long totalAvailableTickets;
    private Long totalSettlements;
    private Long totalPendingSettlements;
    private Long totalSuccessfulSettlements;
    private Long totalCancelledSettlements;
    private Long totalFailedSettlements;
    private Long totalSales;
    private Double totalSettledAmount;

    @JsonGetter(value = "totalCapacity")
    public Long getTotalCapacity() {
        return ObjectUtils.defaultIfNull(totalCapacity, 0L);
    }

    @JsonGetter(value = "totalTicketSections")
    public Long getTotalTicketSections() {
        return ObjectUtils.defaultIfNull(totalTicketSections, 0L);
    }

    @JsonGetter(value = "totalAcquiredTickets")
    public Long getTotalAcquiredTickets() {
        return ObjectUtils.defaultIfNull(totalAcquiredTickets, 0L);
    }

    @JsonGetter(value = "totalAvailableTickets")
    public Long getTotalAvailableTickets() {
        return ObjectUtils.defaultIfNull(totalAvailableTickets, 0L);
    }

    @JsonGetter(value = "totalSettlements")
    public Long getTotalSettlements() {
        return ObjectUtils.defaultIfNull(totalSettlements, 0L);
    }

    @JsonGetter(value = "totalPendingSettlements")
    public Long getTotalPendingSettlements() {
        return ObjectUtils.defaultIfNull(totalPendingSettlements, 0L);
    }

    @JsonGetter(value = "totalSuccessfulSettlements")
    public Long getTotalSuccessfulSettlements() {
        return ObjectUtils.defaultIfNull(totalSuccessfulSettlements, 0L);
    }

    @JsonGetter(value = "totalCancelledSettlements")
    public Long getTotalCancelledSettlements() {
        return ObjectUtils.defaultIfNull(totalCancelledSettlements, 0L);
    }

    @JsonGetter(value = "totalFailedSettlements")
    public Long getTotalFailedSettlements() {
        return ObjectUtils.defaultIfNull(totalFailedSettlements, 0L);
    }

    @JsonGetter(value = "totalSales")
    public Long getTotalSales() {
        return ObjectUtils.defaultIfNull(totalSales, 0L);
    }

    @JsonGetter(value = "totalSettledAmount")
    public Double getTotalSettledAmount() {
        return ObjectUtils.defaultIfNull(totalSettledAmount, 0D);
    }
}