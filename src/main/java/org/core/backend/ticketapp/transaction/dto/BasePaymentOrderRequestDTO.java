package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasePaymentOrderRequestDTO {
    @NotNull(message = "Event Id can't be null")
    private UUID eventId;
    @NotNull(message = "seatSectionId Required")
    private UUID seatSectionId;
    @NotNull(message = "Email cannot be null")
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String plan;
    @JsonIgnore
    private double amount;
}
