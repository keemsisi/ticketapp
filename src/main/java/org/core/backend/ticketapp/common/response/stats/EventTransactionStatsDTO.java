package org.core.backend.ticketapp.common.response.stats;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventTransactionStatsDTO extends EventStatsDTO {
    private Long totalStatusAmount;//all status amounts!
    private Double totalSettledAmount;
    private Long totalSettlements;
    private Double totalEventTicketAmountWithFees;


    @JsonGetter(value = "totalSettlements")
    public Long getTotalSettlements() {
        return ObjectUtils.defaultIfNull(totalSettlements, 0L);
    }


    @JsonGetter(value = "totalStatusAmount")
    public Long getTotalSalesAmount() {
        return ObjectUtils.defaultIfNull(totalStatusAmount, 0L);
    }

    @JsonGetter(value = "totalSettledAmount")
    public Double getTotalSettledAmount() {
        return ObjectUtils.defaultIfNull(totalSettledAmount, 0D);
    }

    @JsonGetter(value = "totalEventTicketAmountWithFees")
    public Double getTotalEventTicketAmountWithFees() {
        return ObjectUtils.defaultIfNull(totalEventTicketAmountWithFees, 0D);
    }

}