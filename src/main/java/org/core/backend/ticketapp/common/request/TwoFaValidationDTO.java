package org.core.backend.ticketapp.common.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFaValidationDTO {
    @NotNull(message = "userId can not be empty")
    private UUID userId;
    @NotNull(message = "email can not be empty")
    private String password;
    @NotNull(message = "otp can not be null")
    private String otp;
}
