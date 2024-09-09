package org.core.backend.ticketapp.passport.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "bank_account_details", indexes = {
        @Index(name = "ix_bnk_acc_dt_user_id_uq", columnList = "user_id", unique = true),
        @Index(name = "ix_bnk_acc_dt_user_id_account_number_uq", columnList = "user_id, account_number", unique = true)
})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDetails extends AbstractBaseEntity {
    @Column(name = "bank_code", columnDefinition = "")
    private String bankCode;
    @NotBlank(message = "bank name can't be blank")
    @Column(name = "bank_name", columnDefinition = "varchar(500) not null")
    private String bankName;
    @NotBlank(message = "account name can't be blank")
    @Column(name = "account_name", columnDefinition = "varchar(500) not null")
    private String accountName;
    @NotBlank(message = "account number can't be blank")
    @Column(name = "account_number", columnDefinition = "varchar(10) not null")
    private String accountNumber;
    @Column(name = "event_id")
    private UUID eventId;
    @Column(name = "reference")
    private String reference;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }

    @PreUpdate
    public void onUpdate() {
        dateModified = LocalDateTime.now();
    }
}
