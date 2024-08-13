package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.plan.entity.Plan;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionInitializeRequestDTO {
        @NotNull(message = "Amount cannot be null")
        @Digits(integer = 6, fraction = 2)
        private BigDecimal amount;

        @NotNull(message = "Email cannot be null")
        private String email;

        private String plan;

        private String currency = "NGN";
}
