package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class UserGroupDto {

        @NotNull
        private List<UUID> userIds;

        @NotNull
        private UUID groupId;
}
