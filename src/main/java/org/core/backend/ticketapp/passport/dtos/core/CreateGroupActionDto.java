package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class CreateGroupActionDto {

        @NotNull
        private List<UUID> actionIds;
        private UUID groupId;
}
