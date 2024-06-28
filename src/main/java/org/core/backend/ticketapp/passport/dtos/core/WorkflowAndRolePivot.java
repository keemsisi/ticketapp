package org.core.backend.ticketapp.passport.dtos.core;

import io.github.thecarisma.CopyProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;


@Data
public class WorkflowAndRolePivot {

    @Id
    @Column(name = "id")
    @CopyProperty(ignore = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @CopyProperty(ignore = true)
    @Column(name = "workflow_id")
    UUID workflowId;

    @CopyProperty(ignore = true)
    @Column(name = "workflow_role_id")
    UUID workflowRoleId;
}
