package org.core.backend.ticketapp.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.Status;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventTransactionDateStatsDTO {
    private Long year;
    private Long month;
    private Status status;
    private BigDecimal totalAmount;
}
