package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ActionDto {
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotNull
        private UUID moduleId;
}
