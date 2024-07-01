package org.core.backend.ticketapp.event.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@Table(name = "event_seat_sections",
        indexes = {@Index(name = "ix_tbl_event_seat_secs_name_event_id_user_id_uq", columnList = "name,event_id,user_id", unique = true)})
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class EventSeatSections extends AbstractBaseEntity {
    @Column(name = "event_id")
    private UUID eventId;
    @Column(name = "user_id", columnDefinition = "uuid not null")
    private UUID userId;
    @Column(columnDefinition = "varchar(255) not null")
    private String name;
    @Column(columnDefinition = "numeric(19)")
    private Long capacity;
    @Column(columnDefinition = "numeric(19)")
    private Long acquired;
    private ApprovalStatus approvalStatus = ApprovalStatus.APPROVED;//later to be updated to go through approval

    public EventSeatSections(UUID id, LocalDateTime dateCreated, UUID createdBy, LocalDateTime dateModified, UUID modifiedBy,
                             @NotNull long index, @NotNull boolean deleted, boolean active, long version) {
        super(id, dateCreated, createdBy, dateModified, modifiedBy, index, deleted, active, version);
    }
}
