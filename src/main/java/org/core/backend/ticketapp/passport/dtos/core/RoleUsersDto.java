package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Data
public class RoleUsersDto implements Serializable {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePictureLocation;
}
