package org.core.backend.ticketapp.passport.dtos.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApplicationConfigType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationConfigRequest {
    @NotNull(message = "config type required")
    private ApplicationConfigType type;
    @NotBlank(message = "Can't be blank")
    private String value;
}
