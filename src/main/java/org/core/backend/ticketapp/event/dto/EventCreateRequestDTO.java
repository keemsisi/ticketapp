package org.core.backend.ticketapp.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.core.backend.ticketapp.event.entity.EventSeatSection;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCreateRequestDTO {
    @NotBlank
    @NotNull
    private String title;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private String location;
    @NotNull
    @NotBlank
    private String eventBanner = "event-banner.jpg";
    private boolean physicalEvent;
    private boolean freeEvent;
    private int locationNumber;
    @NotNull
    @NotBlank
    private String streetAddress;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventDate;
    private boolean approvalRequired;
    private boolean recurring = false;
    @NotNull
    private TimeZoneEnum timeZone;
    @NotNull
    private List<EventSeatSection> seatSections;
    @NotNull
    private Set<String> subCategories;
    @NotNull
    private EventTicketType ticketType;
    @NotBlank
    @NotNull
    private String eventCategory;
}
