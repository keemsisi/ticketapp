package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "department", indexes = {@Index(name = "ix_tbl_department_col_code_uq", columnList = "code", unique = true),
        @Index(name = "ix_tbl_department_col_name_uq", columnList = "normalized_name", unique = true)})
public class PricingSubscription {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "code", nullable = false)
    private String code;

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
