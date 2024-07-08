package org.core.backend.ticketapp.event.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventSeatSectionsCreateRequestDTO(
        @NotNull String type,
        @NotNull Long capacity,
        @NotNull UUID eventId
) {
}
