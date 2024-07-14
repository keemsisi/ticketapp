package org.core.backend.ticketapp.event.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventUpdateRequestDTO(
        @NotNull UUID id,
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
        @NotNull LocalTime eventTimex) {
}
