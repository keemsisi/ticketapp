package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;


@Data
public class UserRoleDto implements Serializable {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID roleId;

}
