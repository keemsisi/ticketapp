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
@Table(name = "event_category")
@Getter
@Setter
public class EventCategory extends AbstractBaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;

    @PrePersist
    public void onCreate() {
        if (this.id == null) this.id = UUID.randomUUID();
        if (this.dateCreated == null) this.dateCreated = LocalDateTime.now();
    }

}
