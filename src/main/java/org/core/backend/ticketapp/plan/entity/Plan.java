package org.core.backend.ticketapp.plan.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan", indexes = {
        @Index(name = "ix_code_name_uq", columnList = "code_name", unique = true),
        @Index(name = "ix_code_n_uq", columnList = "code_name", unique = true),
})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
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

    @Column(name = "plan_code", nullable = false)
    private String planCode;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @NotNull(message = "Amount cannot be null")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;

    @Type(type = "JSONB")
    @Column(name = "categories", columnDefinition = "JSONB")
    private List<String> features;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
        codeName = name.toUpperCase();
    }

}
