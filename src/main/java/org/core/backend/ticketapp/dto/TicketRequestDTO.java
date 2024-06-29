package org.core.backend.ticketapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDTO {
    private Long id;
    private String type;
    private double price;
    private Long eventId;
}
