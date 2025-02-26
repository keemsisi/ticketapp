package org.core.backend.ticketapp.transaction.dto.payment_request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.transaction.entity.request.PaymentRequestStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaymentRequestRequest {
    @NotNull
    private UUID id;
    @NotNull
    private PaymentRequestStatus paymentRequestStatus;
}
