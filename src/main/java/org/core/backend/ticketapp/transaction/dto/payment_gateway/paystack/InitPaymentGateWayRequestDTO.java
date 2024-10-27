package org.core.backend.ticketapp.transaction.dto.payment_gateway.paystack;

import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitPaymentGateWayRequestDTO implements Serializable, Cloneable {
    @NotNull(message = "Amount cannot be null")
    private double amount;
    @NotNull(message = "Email cannot be null")
    private String email;
    private String plan;
    @Value("${system.payment.vendor.paystack.callback}")
    private String callback;
    @Value("${system.payment.vendor.paystack.channels}")
    private String[] channels;

    @Override
    public InitPaymentGateWayRequestDTO clone() {
        try {
            return new InitPaymentGateWayRequestDTO(this.amount, this.email, this.plan, this.callback, this.channels);
        } catch (final Exception e) {
            throw new AssertionError();
        }
    }
}
