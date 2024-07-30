package org.core.backend.ticketapp.transaction.entity;

import lombok.*;
import org.core.backend.ticketapp.common.enums.PricingPlanType;
import org.core.backend.ticketapp.passport.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @Column(columnDefinition = "UUID NOT NULL default uuid_generate_v1()")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "reference", unique = true)
    private String reference;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "gateway")
    private String gateway;

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "channel")
    private String channel;

    @Column(name = "currency")
    private String currency;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "pricing_plan")
    @Enumerated(EnumType.STRING)
    private PricingPlanType pricingPlan;

    @Column(name = "paid_at")
    private String paidAt;

    @Column(name = "payment_date")
    private Timestamp paymentDate;

    @Column(name = "created_at")
    private String createdAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;
}
