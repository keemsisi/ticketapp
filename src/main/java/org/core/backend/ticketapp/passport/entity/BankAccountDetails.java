package org.core.backend.ticketapp.passport.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDetails extends AbstractBaseEntity {
    private String bankCode;
    @NotBlank(message = "bank name can't be blank")
    private String bankName;
    @NotBlank(message = "account name can't be blank")
    private String accountName;
    @NotBlank(message = "account number can't be blank")
    private String accountNumber;
}
