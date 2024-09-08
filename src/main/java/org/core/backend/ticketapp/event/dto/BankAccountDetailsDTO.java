package org.core.backend.ticketapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDetailsDTO {
    private String bankCode;
    @NotBlank(message = "bank name can't be blank")
    private String bankName;
    @NotBlank(message = "account name can't be blank")
    private String accountName;
    @NotBlank(message = "account number can't be blank")
    private String accountNumber;
    private boolean isDefault;
}
