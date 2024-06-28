package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
public class WorkflowModules {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "module_id")
    private UUID moduleId;

    @Column(name = "workflow_id")
    private UUID workflowId;

    @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private DateTime createdOn;

    @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private DateTime modifiedOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private int isDeleted;
}
