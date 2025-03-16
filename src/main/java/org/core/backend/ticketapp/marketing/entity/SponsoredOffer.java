package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sponsored_offer")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class SponsoredOffer extends AbstractBaseEntity {
    @Column(name = "title", columnDefinition = "varchar(250)", nullable = false)
    private String title;

    @Column(name = "valid_from", columnDefinition = "timestamptz", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_to", columnDefinition = "timestamptz", nullable = false)
    private LocalDateTime validTo;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "discount")
    private BigDecimal discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", columnDefinition = "varchar(15) default 'PERCENTAGE'")
    private DiscountType discountType;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
        discountType = ObjectUtils.defaultIfNull(discountType, DiscountType.PERCENTAGE);
    }

    public enum DiscountType {
        PERCENTAGE, FIXED
    }
}
