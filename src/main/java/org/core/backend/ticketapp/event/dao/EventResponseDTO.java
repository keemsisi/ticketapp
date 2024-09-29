package org.core.backend.ticketapp.event.dao;

import lombok.*;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.core.backend.ticketapp.event.entity.EventSeatSection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EventResponseDTO {
    protected LocalDateTime dateCreated;
    protected UUID userId;
    protected LocalDateTime dateModified;
    protected UUID modifiedBy;
    @NotNull
    protected long index;
    @NotNull
    protected boolean deleted;
    private long version;
    private UUID tenantId;
    private UUID id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private boolean physicalEvent;
    private boolean freeEvent;
    private int ticketsAvailable;
    private int maxPerUser;
    private String location;
    private String locationNumber;
    private String streetAddress;
    private HashSet<String> categories;
    private String eventBanner;
    private String theme;
    private boolean recurring;
    private String timeZone;
    private LocalDateTime eventDate;
    private ApprovalStatus approvalStatus;
    private EventTicketType ticketType;
    private boolean approvalRequired;
    private List<EventSeatSection> seatSections;
}
