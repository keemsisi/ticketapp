package org.core.backend.ticketapp.ticket.dto;

import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.TicketStatus;

import java.util.UUID;

@Getter
@Setter
public class TicketRequestDTO {
    private UUID id;
    private double price;
    private UUID eventId;
    private String seatSection;
    private UUID userId;
    private String firstName;
    private String lstName;
    private String email;
    private String phoneNumber;
}
