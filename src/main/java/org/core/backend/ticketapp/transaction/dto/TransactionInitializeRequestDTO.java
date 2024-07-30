package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionInitializeRequestDTO {
        @NotNull(message = "Amount cannot be null")
        private BigDecimal amount;

        @NotNull(message = "Email cannot be null")
        private String email;

//        private String currency,
//        private String plan,
//        private String[] channels
}
