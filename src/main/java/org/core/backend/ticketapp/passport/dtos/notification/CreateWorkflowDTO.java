package org.core.backend.ticketapp.passport.dtos.notification;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateWorkflowDTO {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private UUID moduleId;
    @NotEmpty
    private List<UUID> actionId;
    private List<@Valid CreateWorkflowLevelDTO> levels = new ArrayList<>();
}