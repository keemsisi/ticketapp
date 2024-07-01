package org.core.backend.ticketapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.core.backend.ticketapp.event.entity.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
    private UUID id;
    private String title;
    private String description;
    private String location;
    private String eventBanner = "event-banner.jpg";
    private boolean physicalEvent;
    private boolean freeEvent;
    private int maxPerPerson;
    private int ticketsAvailable;
    private int locationNumber;
    private String streetAddress;
    private EventCategoryEnum eventCategory;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private UUID userId;
    private String approvalStatus;
    private boolean approvalRequired;
    private boolean recurring = false;
    private TimeZoneEnum timeZone;
    private List<Event.SeatSection> seatSections;
}
