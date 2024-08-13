package org.core.backend.ticketapp.subscription.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.OrderStatus;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class Subscription extends AbstractBaseEntity {

    @Column(name = "vendor_id")
    private UUID vendorId;

    @Column(name = "vendor_link")
    private String vendorLink;

    @Column(name = "vendor_email")
    private UUID vendorEmail;

    @Column(name = "is_active")
    private boolean isActive;

    @Column( name = "subscription_code")
    private String subscriptionCode;
}
