package org.core.backend.ticketapp.common.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResetPassword {

    @NotBlank(message = "The new password cannot be empty")
    String password;

    @NotBlank(message = "The token cannot be empty")
    String token;
}
