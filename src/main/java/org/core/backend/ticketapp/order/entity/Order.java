package org.core.backend.ticketapp.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.Gender;
import org.core.backend.ticketapp.common.enums.OrderStatus;
import org.core.backend.ticketapp.common.enums.OrderType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class Order extends AbstractBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "varchar(255) default 'TICKET'")
    private OrderType type;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "total_batch_amount")
    private BigDecimal totalBatchAmount;

    @JsonIgnore
    @Column(name = "batch_order_id")
    private UUID batchOrderId;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "meta", columnDefinition = "JSONB")
    @Type(type = "JSONB")
    private Map<String, String> meta;

    @Column(name = "payment_link")
    private String paymentLink;

    @Column(name = "code")
    private String code;

    @Column(name = "reference")
    private String reference;

    @Column(name = "batch_id")
    private UUID batchId;

    @Column(name = "section_id")
    private UUID seatSectionId;

    @Column(name = "ticket_id", columnDefinition = "uuid default null")
    private UUID ticketId;

    @Column(name = "is_primary", columnDefinition = "boolean default false")
    private boolean isPrimary;

    @JsonIgnore
    @Transient
    private String email;
    @JsonIgnore
    @Transient
    private String firstName;
    @JsonIgnore
    @Transient
    private String lastName;
    @JsonIgnore
    @Transient
    private String phone;
    @JsonIgnore
    @Transient
    private Gender gender;

    @PrePersist
    public void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID();
        if (this.dateCreated == null) this.dateCreated = LocalDateTime.now();
    }
}
