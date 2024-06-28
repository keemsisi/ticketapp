package org.core.backend.ticketapp.passport.entity;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "workflow_level", indexes = {@Index(name = "ix_tbl_workflow_level_col_workflow_id__name_uq", columnList = "workflow_id, normalized_name", unique = true),
        @Index(name = "ix_tbl_workflow_level_col_workflow_id__level_no_uq", columnList = "workflow_id, level_no", unique = true)})
@Data
@TypeDefs({
        @TypeDef(name = "workflow_approval_status_enum", typeClass = PostgreSQLEnumType.class),
        @TypeDef(name = "UUID", typeClass = PostgresUUIDType.class),

})
public class WorkflowLevels {
        @Id
        @Column(name = "id", columnDefinition = "UUID")
        private UUID id;

        @Column(name = "workflow_id")
        private UUID workflowId;

        @Column(name = "role_id")
        private UUID roleId;

        @Column(name = "name")
        private String name;

        @Column(name = "normalized_name")
        private String normalizedName;

        @Column(name = "description")
        private String description;

        @Column(name = "level_no")
        private Integer levelNo;

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
