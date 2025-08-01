package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChangePassword {
    @NotNull @NotEmpty
    @NotBlank(message = "The old password value cannot be empty")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotNull @NotEmpty
    @NotBlank(message = "The new password value cannot be empty")
    @JsonProperty("new_password")
    private String newPassword;
}
