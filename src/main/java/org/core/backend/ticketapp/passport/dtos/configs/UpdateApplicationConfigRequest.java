package org.core.backend.ticketapp.passport.dtos.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationConfigRequest extends CreateApplicationConfigRequest {
    @NotNull(message = "id is required!")
    private UUID id;
}
