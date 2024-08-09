package org.core.backend.ticketapp.ticket.entity;

import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "qr_code")
public class QrCode extends AbstractBaseEntity {
    @Id
    @Column(columnDefinition = "uuid not null default uuid_generate_v4()")
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "ticket_id", nullable = false, unique = true)
    private UUID ticketId;

    @PrePersist
    public void onCreate() {
        id = UUID.randomUUID();
        if (dateCreated == null) {
            dateCreated = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        if (dateModified == null) {
            dateModified = LocalDateTime.now();
        }
    }
}
