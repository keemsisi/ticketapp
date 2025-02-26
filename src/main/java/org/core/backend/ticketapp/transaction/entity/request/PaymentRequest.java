package org.core.backend.ticketapp.transaction.entity.request;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.transaction.entity.wallet.Wallet;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Table(name = "payment_request",
        indexes = {@Index(name = "ix_payment_request_user_id_event_id_uq", columnList = "user_id, event_id", unique = true)})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class PaymentRequest extends AbstractBaseEntity {

    @Column(name = "event_id", columnDefinition = "uuid")
    private UUID eventId;

    @Column(name = "wallet_id", columnDefinition = "uuid")
    private UUID walletId;

    @Column(name = "total_amount", columnDefinition = "numeric(19,2) not 0.0")
    private BigDecimal totalAmount;

    @Column(name = "total_paid", columnDefinition = "numeric(19,2) 0.0")
    private BigDecimal totalPaid;

    @Column(name = "request_date", columnDefinition = "timestamptz not null default now()")
    private LocalDateTime requestDate;

    @Column(name = "last_paid_date", columnDefinition = "timestamptz")
    private LocalDateTime lastPaidDate;

    @Column(name = "type", columnDefinition = "varchar(25) not null default 'EVENT_SETTLEMENT'")
    private PaymentRequestType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'PENDING'")
    private PaymentRequestStatus status;

    @Column(name = "meta", columnDefinition = "JSONB default null")
    @Type(type = "JSONB")
    private Map<String, Object> meta; // this can contain the track records of the payment

    @Column(name = "unique_reference", columnDefinition = "varchar(255)")
    private String uniqueReference;

    @Transient
    private Wallet wallet;
}
