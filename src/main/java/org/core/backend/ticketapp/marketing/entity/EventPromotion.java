package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_promotion")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class EventPromotion extends AbstractBaseEntity {
    @Column(name = "event_id", nullable = false)
    private UUID eventId;
    @NotBlank(message = "title can't be blank")
    private String title;
    @NotBlank(message = "description can't be blank")
    private String description;
    @NotBlank(message = "image can't be blank")
    private String image;
    @NotBlank(message = "callToAction can't be blank")
    private String callToAction;
    @NotNull(message = "startDate can't be blank")
    @Column(name = "start_date", columnDefinition = "timestamptz")
    private LocalDateTime startDate;
    @NotNull(message = "endDate can't be blank")
    @Column(name = "end_date", columnDefinition = "timestamptz")
    private LocalDateTime endDate;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}
