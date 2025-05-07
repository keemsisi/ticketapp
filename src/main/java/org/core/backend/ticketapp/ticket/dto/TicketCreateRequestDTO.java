package org.core.backend.ticketapp.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.Gender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateRequestDTO {
    @NotNull(message = "eventId can't be null")
    private UUID eventId;
    @NotNull(message = "seatSectionId can't be null")
    private UUID seatSectionId;
    @NotNull(message = "userId can't be null")
    private UUID userId;
    @NotNull(message = "firstName can't be blank")
    private String firstName;
    @NotBlank(message = "lastName can't be blank")
    private String lastName;
    @NotBlank(message = "email can't be blank")
    private String email;
    @NotBlank(message = "phone number can't be blank")
    private String phoneNumber;
    private Gender gender;
}
