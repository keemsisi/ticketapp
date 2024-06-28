package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
public class WorkflowPermission implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @Column(name = "value", insertable = false, updatable = false)
    String value;

    @Column(name = "workflow_id")
    UUID workflowId;

    @Column(name = "permission_id")
    UUID permissionId;

    @Transient
    List<UUID> permissionIds = new ArrayList<>();
}
