package org.core.backend.ticketapp.event.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_wishlist")
@Getter
@Setter
public class EventWishList extends AbstractBaseEntity {
    @Column(nullable = false, name = "event_id")
    private UUID eventId;
    @Column(nullable = false)
    private UUID userId;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        if (dateCreated == null) LocalDateTime.now();
    }
}
