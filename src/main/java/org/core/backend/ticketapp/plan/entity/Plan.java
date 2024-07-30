package org.core.backend.ticketapp.plan.entity;

import lombok.*;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @Column(columnDefinition = "UUID NOT NULL default uuid_generate_v1()")
    protected UUID id;

    @Column(name = "name")
    private String name;

    @NotNull(message = "Interval cannot be null")
    private String interval;

    @NotNull(message = "Amount cannot be null")
    @Digits(integer = 6, fraction = 2)
    private Integer amount;

    @Column(name = "usage_limit")
    private String usageLimit;

}
