package org.core.backend.ticketapp.event.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "event")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class Event extends AbstractBaseEntity {

    @Id
    @Column(columnDefinition = "uuid not null default uuid_generate_v4()")
    private UUID id;
    @NotBlank private String title;
    @NotBlank private String description;
    @NotNull private boolean physicalEvent;
    @NotNull private boolean freeEvent;
    @NotNull private int ticketsAvailable;
    @NotNull private int maxPerUser;
    @NotNull private String location;
    @NotNull private String locationNumber;
    @NotNull private String streetAddress;
    @NotNull private EventCategoryEnum eventCategory;

    private String eventBanner = "event-banner.jpg";
    private boolean recurring = false;

    @NotNull private TimeZoneEnum timeZone = TimeZoneEnum.WAT;
    @NotNull private LocalDate eventDate;
    @NotNull private LocalTime eventTime;
    @Column(name = "user_id", nullable = false) private UUID userId;
    @Enumerated(EnumType.STRING) private ApprovalStatus approvalStatus;

    private boolean approvalRequired;

    @Column(name = "seat_sections", columnDefinition = "JSONB")
    @Type(type = "JSONB")
    private List<EventSeatSection> seatSections;
}
