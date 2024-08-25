package org.core.backend.ticketapp.plan.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_wishlist", indexes = {
        @Index(name = "ix_event_id__user_id_uq", columnList = "event_id, user_id", unique = true)})
public class Plan extends AbstractBaseEntity {
    @Id
    @Column(columnDefinition = "UUID NOT NULL default uuid_generate_v1()")
    protected UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "code_name")
    private String codeName;

    @NotNull(message = "Interval cannot be null")
    private String interval;

    @Column(name = "plan_code")
    private String planCode;

    @Column(name = "plan_id")
    private String planId;

    @NotNull(message = "Amount cannot be null")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }

}
