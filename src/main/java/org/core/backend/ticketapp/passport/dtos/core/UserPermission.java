package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Data
public class UserPermission implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "value", insertable = false, updatable = false)
    private String value;

    @JsonIgnore
    //@Column(name = "user_id")
    private UUID userId;

    //@Column(name = "permission_id")
    private UUID permissionId;
}
