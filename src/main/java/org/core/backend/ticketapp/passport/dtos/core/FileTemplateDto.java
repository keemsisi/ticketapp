package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class FileTemplateDto {
    @NotBlank(message = "The name cannot be empty")
    private String name;

    private String description;

    private String url;
}
