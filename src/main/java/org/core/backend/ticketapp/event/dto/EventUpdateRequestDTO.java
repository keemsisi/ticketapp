package org.core.backend.ticketapp.event.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
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
        @NotNull Set<String> categories,
        String eventBanner,
        boolean recurring,
        @NotNull LocalDate eventDate,
        @NotNull LocalTime eventTimex) {
}
