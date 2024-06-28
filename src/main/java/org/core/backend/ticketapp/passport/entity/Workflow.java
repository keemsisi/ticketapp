package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "workflow", indexes = {@Index(name = "ix_tbl_workflow_col_name_uq", columnList = "normalized_name", unique = true)})
public class Workflow {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Transient
    @Column(name = "action_id")
    private UUID actionId;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "active")
    private boolean active;

    @Column(name = "description")
    private String description;

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

    @Column(name = "module_id")
    private UUID moduleId;

    @Transient
    private List<WorkflowLevels> workflowLevels = new ArrayList<>();
}
