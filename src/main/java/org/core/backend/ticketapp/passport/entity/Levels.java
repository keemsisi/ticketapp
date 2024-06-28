package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "level", indexes = {@Index(name = "ix_tbl_level_col_code_uq", columnList = "code", unique = true),
        @Index(name = "ix_tbl_level_col_name_uq", columnList = "normalized_name", unique = true)})
public class Levels {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_id")
    @NotNull
    private UUID unitId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean isDeleted;
}
