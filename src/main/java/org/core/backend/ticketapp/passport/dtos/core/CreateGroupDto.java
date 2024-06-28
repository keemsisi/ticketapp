package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class CreateGroupDto {
        @NotBlank
        private String name;
        @NotBlank
        private String code;
        @NotBlank
        private String description;
}
