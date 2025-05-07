package org.core.backend.ticketapp.transaction.service.payment.paystack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinalizeTransferRequestDTO {
    @JsonProperty(value = "transfer_code")
    @NotBlank(message = "transferCode can't be blank")
    private String transferCode;
    @NotBlank(message = "otp can't be blank")
    private String otp;
}
