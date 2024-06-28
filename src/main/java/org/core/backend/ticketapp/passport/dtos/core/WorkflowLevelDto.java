package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;
import org.joda.time.DateTime;
import javax.persistence.*;
import java.util.UUID;

@Data
public class WorkflowLevelDto {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "workflow_id")
        private UUID workflowId;

        @Column(name = "role_id")
        private UUID roleId;

        @Column(name = "name")
        private String name;

        @Column(name = "conditions")
        private String condition;

        @Column(name = "level_no")
        private Integer levelNo;

        @Column(name = "condition_value")
        private String conditionValue;

        @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private DateTime createdOn;

        @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private DateTime modifiedOn;

        @Column(name = "created_by")
        private UUID createdBy;

        @Column(name = "modified_by")
        private UUID modifiedBy;

        @Column(name = "is_deleted")
        private int isDleted;
}
