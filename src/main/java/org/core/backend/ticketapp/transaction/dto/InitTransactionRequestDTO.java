package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InitTransactionRequestDTO {
    @NotNull(message = "Amount cannot be null")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;
    private String plan;
    @NotNull(message = "Event Id can't be null")
    private UUID eventId;
    @Min(value = 1, message = "Minimum persons is 1!")
    private Integer quantity;
    @Min(value = 1, message = "Seat section Id Required")
    private UUID seatSectionId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @NotNull(message = "Email cannot be null")
    private String email;
}
