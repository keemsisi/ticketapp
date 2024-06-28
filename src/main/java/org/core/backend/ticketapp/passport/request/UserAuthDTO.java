package org.core.backend.ticketapp.passport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO {
    @NotNull(message = "email can not be null")
    @NotEmpty(message = "email can not be empty")
    private String email;
    @NotNull(message = "password can not be null")
    @NotEmpty(message = "password can not be empty")
    private String password;
}
