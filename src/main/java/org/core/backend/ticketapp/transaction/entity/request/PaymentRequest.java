package org.core.backend.ticketapp.transaction.entity.request;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
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
@OptimisticLocking(type = OptimisticLockType.VERSION)
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class PaymentRequest extends AbstractBaseEntity {

    @Column(name = "event_id", columnDefinition = "uuid not null")
    private UUID eventId;

    @Column(name = "total_amount", columnDefinition = "numeric(19,2) not null")
    private BigDecimal totalAmount;

    @Column(name = "total_paid", columnDefinition = "numeric(19,2) null")
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
}
