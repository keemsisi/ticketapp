package org.core.backend.ticketapp.passport.dtos.core;

import io.github.thecarisma.CopyProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@Data
public class Levels {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "description")
    private String description;

    @CopyProperty(ignore = true)
    @Column(name = "unit_id")
    private UUID unitId;

    @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date createdOn;

    @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date modifiedOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
