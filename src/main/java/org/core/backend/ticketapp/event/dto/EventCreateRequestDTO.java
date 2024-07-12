package org.core.backend.ticketapp.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.core.backend.ticketapp.event.entity.EventSeatSection;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventDate;
    private String approvalStatus;
    private boolean approvalRequired;
    private boolean recurring = false;
    private TimeZoneEnum timeZone;
    private List<EventSeatSection> seatSections;
    private EventTicketType ticketType;
}
