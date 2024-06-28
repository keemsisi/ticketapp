package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
public class GroupModuleActions {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "group_id")
        private UUID groupId;

        @Column(name = "module_id")
        private UUID moduleId;

        @Column(name = "actions")
        private String actions;

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

        @Transient
        private String[] actionsInArray;
}
