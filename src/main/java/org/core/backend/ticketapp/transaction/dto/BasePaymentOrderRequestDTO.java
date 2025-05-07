package org.core.backend.ticketapp.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasePaymentOrderRequestDTO {
    @NotNull(message = "Event Id can't be null")
    private UUID eventId;
    @NotNull(message = "seatSectionId Required")
    private UUID seatSectionId;
    @NotBlank(message = "Email cannot be null")
    private String email;
    @NotBlank(message = "First name cannot be null")
    private String firstName;
    @NotBlank(message = "Last name cannot be null")
    private String lastName;
    private String phoneNumber;
    private String plan;
    @JsonIgnore
    private double amount;
}
