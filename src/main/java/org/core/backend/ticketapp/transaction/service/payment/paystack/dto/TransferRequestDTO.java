package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.passport.service.core.PaymentProcessorType;
import org.core.backend.ticketapp.transaction.service.payment.dto.ProcessorPaymentRequestDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO implements ProcessorPaymentRequestDTO {
    @NotBlank(message = "source can't be blank")
    private String source;
    @NotBlank(message = "reason can't be blank")
    private String reason;
    @NotNull(message = "amount can't be empty")
    private BigDecimal amount;
    @NotBlank(message = "recipient can't be empty")
    private String recipient;
    @JsonIgnore
    private PaymentProcessorType paymentProcessorType;
}
