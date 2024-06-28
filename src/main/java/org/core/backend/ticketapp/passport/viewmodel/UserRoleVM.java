package org.core.backend.ticketapp.passport.viewmodel;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserRoleVM {
    
    UUID id;
    UUID userId;
    UUID roleId;
    String createdBy;
    String modifiedBy;
    Date createdOn;
    Date modifiedOn;
    int isDeleted;
    String firstName;
    String lastName;
}
