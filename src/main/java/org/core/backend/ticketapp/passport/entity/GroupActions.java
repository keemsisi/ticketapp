package org.core.backend.ticketapp.passport.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "group_action", indexes = {@Index(name = "ix_tbl_group_action_col_action_id__group_id_uq", columnList = "action_id, group_id", unique = true)})
public class GroupActions {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "group_id")
        private UUID groupId;

        @Column(name = "action_id")
        private UUID actionId;

        @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private Date createdOn;

        @Column(name = "created_by")
        private UUID createdBy;

        @Column(name = "is_deleted")
        private boolean isDeleted;
}
