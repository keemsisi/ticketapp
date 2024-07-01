package org.core.backend.ticketapp.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventSeatSectionsUpdateRequestDTO(
        String name,
        Long capacity
) {
}
