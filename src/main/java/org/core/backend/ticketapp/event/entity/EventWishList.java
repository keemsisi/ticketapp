package org.core.backend.ticketapp.event.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_wishlist", indexes = {
        @Index(name = "ix_event_id__user_id_uq", columnList = "event_id, user_id", unique = true)})
@Getter
@Setter
public class EventWishList extends AbstractBaseEntity {
    @Column(nullable = false, name = "event_id")
    private UUID eventId;
    @Column(nullable = false, name = "user_id")
    private UUID userId;

    @PrePersist
    protected void onCreate() {
        this.id = ObjectUtils.defaultIfNull(this.id, UUID.randomUUID());
        this.dateCreated = ObjectUtils.defaultIfNull(this.dateCreated, LocalDateTime.now());
    }
}
