package org.core.backend.ticketapp.event.dto;

import javax.validation.constraints.NotNull;

public record SeatSectionDTO(
        @NotNull String type,
        @NotNull Long capacity,
        @NotNull double price) {
}