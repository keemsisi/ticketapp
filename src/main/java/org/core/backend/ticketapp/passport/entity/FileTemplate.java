package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "file_template", indexes = {@Index(name = "ix_tbl_file_template_col_name_uq", columnList = "normalized_name", unique = true)})
public class FileTemplate {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Column(name = "url")
    @NotBlank(message = "The url cannot be empty")
    private String url;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "description")
    private String description;

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
