package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitPaymentOrderRequestDTO {
    @NotNull(message = "primary field is required!")
    private BasePaymentOrderRequestDTO primary;
    private List<BasePaymentOrderRequestDTO> secondary;
    @JsonIgnore
    private boolean free = false;
}
