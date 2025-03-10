package org.core.backend.ticketapp.common.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventOrderStatsDTO extends EventStatsDTO {
    private Long totalOrders;
    private Double totalCompletedOrderAmount;

    @JsonGetter(value = "totalOrders")
    public Long getTotalOrders() {
        return ObjectUtils.defaultIfNull(totalOrders, 0L);
    }

    @JsonGetter("totalOrderAmount")
    public Double getTotalCompletedOrderAmount() {
        return ObjectUtils.defaultIfNull(totalCompletedOrderAmount, 0D);
    }

}