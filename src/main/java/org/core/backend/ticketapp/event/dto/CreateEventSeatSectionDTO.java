package org.core.backend.ticketapp.event.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateEventSeatSectionDTO(@NotNull UUID eventId, @NotNull List<SeatSectionDTO> seatSections) {
}
