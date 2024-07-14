package org.core.backend.ticketapp.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "event")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class Event extends AbstractBaseEntity {

    @Id
    @Column(columnDefinition = "uuid not null default uuid_generate_v4()")
    private UUID id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private boolean physicalEvent;
    @NotNull
    private boolean freeEvent;
    @NotNull
    private int ticketsAvailable;
    @NotNull
    private int maxPerUser;
    @NotNull
    private String location;
    @NotNull
    private String locationNumber;
    @NotNull
    private String streetAddress;
    @NotNull
    private EventCategoryEnum eventCategory;

    private String eventBanner = "event-banner.jpg";
    private boolean recurring = false;

    @NotNull
    private TimeZoneEnum timeZone = TimeZoneEnum.WAT;
    @NotNull
    private LocalDateTime eventDate;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private EventTicketType ticketType;

    private boolean approvalRequired;
    @Transient
    private List<EventSeatSection> seatSections;

    @PrePersist
    public void onCreate() {
        if (approvalStatus == null) approvalStatus = ApprovalStatus.APPROVED;
        if (id == null) id = UUID.randomUUID();
        if (dateCreated == null) LocalDateTime.now();
    }
}
