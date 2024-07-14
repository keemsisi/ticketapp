package org.core.backend.ticketapp.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@Table(name = "event_seat_sections", indexes = {@Index(name = "ix_tbl_event_seat_secs_type_event_id_user_id_uq", columnList = "type,event_id,user_id", unique = true)})
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class EventSeatSection extends AbstractBaseEntity {
    @Column(name = "event_id")
    private UUID eventId;
    @Column(columnDefinition = "varchar(255) not null")
    private String type;
    @Column(columnDefinition = "numeric(19)")
    private Long capacity;
    @Column(columnDefinition = "numeric(19)")
    private Long acquired;
    private double price;
    private ApprovalStatus approvalStatus; //later to be updated to go through approval

    public EventSeatSection(UUID id, LocalDateTime dateCreated, UUID createdBy, LocalDateTime dateModified, UUID modifiedBy,
                            @NotNull long index, @NotNull boolean deleted, long version) {
        super(id, dateCreated, createdBy, dateModified, modifiedBy, index, deleted, version);
    }

    public EventSeatSection(UUID eventId, UUID userId, String type, Long capacity, double price, Long acquired, ApprovalStatus approvalStatus) {
        this.eventId = eventId;
        this.userId = userId;
        this.type = type;
        this.capacity = capacity;
        this.price = price;
        this.acquired = acquired;
        this.approvalStatus = approvalStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSeatSection that = (EventSeatSection) o;
        return Double.compare(price, that.price) == 0 && Objects.equals(eventId, that.eventId) && Objects.equals(type, that.type) && Objects.equals(capacity, that.capacity) && Objects.equals(acquired, that.acquired) && approvalStatus == that.approvalStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, type, capacity, acquired, price, approvalStatus);
    }

    @PrePersist
    public void onCreate() {
        if (id == null) id = UUID.randomUUID();
        if (dateCreated == null) LocalDateTime.now();
        this.approvalStatus = ApprovalStatus.APPROVED;
    }
}
