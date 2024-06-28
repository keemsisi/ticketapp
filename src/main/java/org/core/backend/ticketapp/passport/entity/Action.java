package org.core.backend.ticketapp.passport.entity;

import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@Table(name = "action", indexes = {@Index(name = "ix_tbl_action_col_code_uq", columnList = "code", unique = true),
        @Index(name = "ix_tbl_action_col_name_uq", columnList = "normalized_name", unique = true)})
public class Action {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "module_id", nullable = false)
    private UUID moduleId;

    @Column(name = "name")
    private String name;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Date createdOn;

    @Column(name = "modified_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Date modifiedOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @PreUpdate
    public void onCreate() {
        if (createdOn == null) createdOn = new Date();
    }

}
