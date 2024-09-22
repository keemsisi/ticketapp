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
    private Long totalOrders;
    private Long totalPending;
    private Long totalSuccessful;
    private Long totalCancelled;
    private Long totalFailed;
    private Long totalSettlements;
    private Long totalSalesAmount;
    private Double totalSettledAmount;

    @JsonGetter(value = "totalOrders")
    public Long getTotalOrders() {
        return ObjectUtils.defaultIfNull(totalOrders, 0L);
    }

    @JsonGetter(value = "totalSettlements")
    public Long getTotalSettlements() {
        return ObjectUtils.defaultIfNull(totalSettlements, 0L);
    }

    @JsonGetter(value = "totalPending")
    public Long getTotalPending() {
        return ObjectUtils.defaultIfNull(totalPending, 0L);
    }

    @JsonGetter(value = "totalSuccessful")
    public Long getTotalSuccessful() {
        return ObjectUtils.defaultIfNull(totalSuccessful, 0L);
    }

    @JsonGetter(value = "totalCancelled")
    public Long getTotalCancelled() {
        return ObjectUtils.defaultIfNull(totalCancelled, 0L);
    }

    @JsonGetter(value = "totalFailed")
    public Long getTotalFailed() {
        return ObjectUtils.defaultIfNull(totalFailed, 0L);
    }

    @JsonGetter(value = "totalSalesAmount")
    public Long getTotalSalesAmount() {
        return ObjectUtils.defaultIfNull(totalSalesAmount, 0L);
    }

    @JsonGetter(value = "totalSettledAmount")
    public Double getTotalSettledAmount() {
        return ObjectUtils.defaultIfNull(totalSettledAmount, 0D);
    }
}