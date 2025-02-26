package org.core.backend.ticketapp.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.dto.configs.pricing.TransactionFeesDTO;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.OrderType;
import org.core.backend.ticketapp.common.enums.Status;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class),})
@Table(name = "transaction")
public class Transaction extends AbstractBaseEntity {
    @JoinColumn(name = "user_id")
    private UUID userId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "reference", unique = true)
    private String reference;

    @Column(name = "amount")
    private BigDecimal amount;

    @Type(type = "JSONB")
    @Column(name = "gateway_meta", columnDefinition = "JSON DEFAULT NULL")
    private PaymentGatewayMeta gateWayMeta;

    @Type(type = "JSONB")
    @Column(name = "meta", columnDefinition = "JSON DEFAULT NULL")
    private Map<String, Object> meta;

    @Column(name = "created_by", columnDefinition = "UUID DEFAULT NULL")
    private UUID createdBy;

    @Column(name = "modified_by", columnDefinition = "UUID DEFAULT NULL")
    private UUID modifiedBy;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "provider_status")
    @Enumerated(value = EnumType.STRING)
    private Status providerStatus;

    @Column(name = "comment")
    private String comment;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Type(type = "JSONB")
    @Column(name = "transaction_fees", columnDefinition = "JSONB")
    private TransactionFeesDTO transactionFees;

    @JsonIgnore
    @Transient
    @Column(name = "sender_account_id")
    private String senderAccountId;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());

    }
}
