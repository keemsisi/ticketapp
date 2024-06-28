package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class GroupActionsDto {

        @NotNull
        private UUID actionId;
        private String code;
        private String description;
        private String name;
}
