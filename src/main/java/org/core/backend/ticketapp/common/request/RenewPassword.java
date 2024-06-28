package org.core.backend.ticketapp.common.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RenewPassword {
    @NotNull
    @NotEmpty
    private String email;

    @NotNull @NotEmpty
    @NotBlank(message = "The old password value cannot be empty")
    private String oldPassword;

    @NotNull @NotEmpty
    @NotBlank(message = "The new password value cannot be empty")
    private String newPassword;
}
