package org.core.backend.ticketapp.ticket.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TicketCreateRequestDTO {
    private double price;
    private UUID eventId;
    private String seatSection;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
