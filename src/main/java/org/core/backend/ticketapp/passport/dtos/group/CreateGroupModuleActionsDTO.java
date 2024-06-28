package org.core.backend.ticketapp.passport.dtos.group;

import java.util.UUID;

import lombok.Data;

@Data
public class CreateGroupModuleActionsDTO {
    
    UUID moduleId;
    String[] actions;
}
