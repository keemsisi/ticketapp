package org.core.backend.ticketapp.transaction.entity.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.Status;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Table(name = "wallet", indexes = {@Index(name = "ix_wallet_reference_uq", columnList = "reference", unique = true), @Index(name = "ix_wallet_user_id_account_number_uq", columnList = "user_id, account_number", unique = true)})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class Wallet extends AbstractBaseEntity {
    @Column(name = "account_number", columnDefinition = "varchar(10)")
    private String accountNumber;

    @Column(name = "account_name", columnDefinition = "varchar(250)")
    private String accountName;

    @Column(name = "balance", columnDefinition = "varchar(10) not null default 0")
    private BigDecimal balance;

    @Column(name = "balance_before", columnDefinition = "numeric(19,2) not null default 0")
    private BigDecimal balanceBefore;

    @Column(name = "last_transaction_amount", columnDefinition = "numeric(19,2) not null default 0")
    private BigDecimal lastTransactionAmount;

    @Column(name = "last_transaction_date", columnDefinition = "timestamptz")
    private LocalDateTime lastTransactionDate;

    @Column(name = "reference", columnDefinition = "varchar(255)")
    private String reference;

    @Column(name = "liened_amount", columnDefinition = "numeric(19,2) not null default 0")
    private BigDecimal lienedAmount;

    @Column(name = "currency", columnDefinition = "varchar(10) not null default 'NGN'")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(100) not null")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(100) not null")
    private WalletType type;
}
