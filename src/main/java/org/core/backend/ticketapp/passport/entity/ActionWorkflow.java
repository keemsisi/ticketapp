package org.core.backend.ticketapp.passport.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;


@Data
@Entity
@Table(name = "action_workflow")
public class ActionWorkflow {

    @Id
    @Column(name = "id")
    private UUID id;


    @NotNull
    @Column(name = "action_id")
    private UUID actionId;

    @NotNull
    @Column(name = "workflow_id")
    private UUID workflowId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    private Date modifiedOn;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Transient
    private String code;

    @Transient
    private String name;
}
