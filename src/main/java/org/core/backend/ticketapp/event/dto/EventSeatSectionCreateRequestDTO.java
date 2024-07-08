package org.core.backend.ticketapp.event.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventSeatSectionCreateRequestDTO(
        @NotNull String name,
        @NotNull Long capacity,
        @NotNull UUID eventId
) {
}
