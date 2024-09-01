package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitPaymentOrderRequestDTO {
    @NotNull(message = "primary field is required!")
    private BasePaymentOrderRequestDTO primary;
    private List<BasePaymentOrderRequestDTO> secondary;
}
