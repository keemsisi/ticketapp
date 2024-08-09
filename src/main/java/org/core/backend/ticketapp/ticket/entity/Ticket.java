package org.core.backend.ticketapp.ticket.entity;

import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.Status;

import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ticket")
public class Ticket  extends AbstractBaseEntity {
    @Id
    @Column(columnDefinition = "uuid not null default uuid_generate_v4()")
    private UUID id;

    @Column(name = "seat_section", nullable = false )
    private UUID seatSectionId;

    @Column(name = "seat_number")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long seatNumber;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "user_id")
    private UUID userId;

    @NotNull private double price = 0.0;

    @Column(name = "event_id", nullable = false)
    private UUID eventId;

    private LocalDateTime dateCreated;

    private LocalDateTime dateModified;

    @PrePersist
    public void onCreate() {
        id = UUID.randomUUID();
    }
}
