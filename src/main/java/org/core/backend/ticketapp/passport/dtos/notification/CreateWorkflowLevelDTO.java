package org.core.backend.ticketapp.passport.dtos.notification;

import java.util.UUID;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateWorkflowLevelDTO {
    private UUID workflowId;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private UUID roleId;
    @NotNull
    private Integer levelNo;
}