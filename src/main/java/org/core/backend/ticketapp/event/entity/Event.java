package org.core.backend.ticketapp.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "event")
@Builder
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class Event extends AbstractBaseEntity {

    @Id
    @Column(columnDefinition = "uuid not null default uuid_generate_v4()")
    private UUID id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;

    @Column(name = "physical_event", columnDefinition = "bool", nullable = false)
    private boolean physicalEvent;

    @Column(name = "free_event", columnDefinition = "bool", nullable = false)
    private boolean freeEvent;

    @Column(name = "tickets_available")
    private int ticketsAvailable;

    @Column(name = "max_per_user")
    private int maxPerUser;

    @NotNull
    private String location;
    @Column(name = "location_number", nullable = false)
    private String locationNumber;

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Type(type = "JSONB")
    @Column(name = "categories", columnDefinition = "JSONB")
    private Set<String> categories;

    private String eventBanner = "event-banner.jpg";

    @Column(columnDefinition = "bool default false")
    private boolean recurring = false;

    @Column(name = "time_zone", nullable = false)
    private TimeZoneEnum timeZone = TimeZoneEnum.WAT;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private EventTicketType ticketType;

    @Column(name = "approval_required", columnDefinition = "bool default false")
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
