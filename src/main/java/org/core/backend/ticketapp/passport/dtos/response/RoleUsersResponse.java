package org.core.backend.ticketapp.passport.dtos.response;

import lombok.Builder;
import lombok.Data;
import org.core.backend.ticketapp.passport.dtos.core.UserAvatar;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class RoleUsersResponse implements Serializable {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private List<UserAvatar> users;
}
