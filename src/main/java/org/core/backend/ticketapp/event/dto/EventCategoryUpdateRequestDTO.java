package org.core.backend.ticketapp.event.dto;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public record EventCategoryUpdateRequestDTO(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull String description) {
}
