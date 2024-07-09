package org.core.backend.ticketapp.event.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.hibernate.annotations.Type;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventUpdateRequestDTO(
        @NotNull String title,
        @NotNull String description,
        @NotNull int ticketsAvailable,
        @NotNull boolean physicalEvent,
        @NotNull boolean freeEvent,
        @NotNull int maxPerUser,
        @NotNull String location,
        @NotNull String locationNumber,
        @NotNull String streetAddress,
        @NotNull EventCategoryEnum eventCategory,
        String eventBanner,
        boolean recurring,
        @NotNull LocalDate eventDate,
        @NotNull LocalTime eventTime,
        @Type(type = "JSONB") List<EventSeatSection> seatSections
) {
}
