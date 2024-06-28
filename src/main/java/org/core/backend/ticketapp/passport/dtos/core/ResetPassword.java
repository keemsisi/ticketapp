package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPassword {

    @NotBlank(message = "The new password cannot be empty")
    String password;

    @NotBlank(message = "The token cannot be empty")
    String token;
}
