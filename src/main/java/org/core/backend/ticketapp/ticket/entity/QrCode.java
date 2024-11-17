package org.core.backend.ticketapp.ticket.entity;

import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "qr_code")
public class QrCode extends AbstractBaseEntity {
    @Id
    private UUID id;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    @Column(name = "ticket_id", nullable = false, unique = true)
    private UUID ticketId;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Transient
    private String link;

    @Column(name = "total_scanned", columnDefinition = "numeric(19) default 0")
    private int totalScanned;

    @Column(name = "scanned", columnDefinition = "boolean default false")
    private Boolean scanned;

    @PrePersist
    public void onCreate() {
        id = UUID.randomUUID();
        if (dateCreated == null) {
            dateCreated = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
    }

    @PreUpdate
    public void onUpdate() {
        if (dateModified == null) {
            dateModified = LocalDateTime.now();
        }
    }

    public int incrementScan() {
        return ++totalScanned;
    }
}
