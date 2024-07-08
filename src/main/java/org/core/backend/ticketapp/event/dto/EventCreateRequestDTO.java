package org.core.backend.ticketapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.core.backend.ticketapp.event.entity.EventSeatSection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCreateRequestDTO {
    private String title;
    private String description;
    private String location;
    private String eventBanner = "event-banner.jpg";
    private boolean physicalEvent;
    private boolean freeEvent;
    private int locationNumber;
    private String streetAddress;
    private EventCategoryEnum eventCategory;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private String approvalStatus;
    private boolean approvalRequired;
    private boolean recurring = false;
    private TimeZoneEnum timeZone;
    private List<EventSeatSection> seatSections;
}
