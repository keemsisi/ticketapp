package org.core.backend.ticketapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.AccountNumberType;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Valid
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
    @Max(value = 10, message = "Max account number length should be 10!")
    @NotBlank(message = "account number can't be blank")
    private String accountNumber;
    @NotBlank(message = "accountNumberType can't be blank")
    private AccountNumberType type = AccountNumberType.NUBAN;
    private boolean isDefault = true;
}
