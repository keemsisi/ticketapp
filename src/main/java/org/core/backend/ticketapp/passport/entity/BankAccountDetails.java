package org.core.backend.ticketapp.passport.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "bank_account_details", indexes = {
        @Index(name = "ix_bnk_acc_dt_user_id_uq", columnList = "user_id", unique = true),
        @Index(name = "ix_bnk_acc_dt_user_id_account_number_uq", columnList = "user_id, account_number", unique = true)
})
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
    private UUID eventId;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }

    @PrePersist
    public void onUpdate() {
        dateModified = LocalDateTime.now();
    }
}
