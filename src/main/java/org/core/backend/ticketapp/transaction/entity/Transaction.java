package org.core.backend.ticketapp.transaction.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.Status;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(name = "reference", unique = true)
    private String reference;

    @Column(name = "amount")
    private BigDecimal amount;

    @Type(type = "JSONB")
    @Column(name = "gateway_meta", columnDefinition = "JSON DEFAULT NULL")
    private PaymentGatewayMeta gateWayMeta;

    @Column(name = "created_by", columnDefinition = "UUID DEFAULT NULL")
    private UUID createdBy;

    @Column(name = "modified_by", columnDefinition = "UUID DEFAULT NULL")
    private UUID modifiedBy;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "comment")
    private String comment;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}
