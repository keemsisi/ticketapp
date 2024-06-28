package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class GroupUsersDto {

        @NotNull
        private UUID userId;

        @NotNull
        private UUID groupId;
}
