package org.core.backend.ticketapp.passport.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "group_user", indexes = {@Index(name = "ix_tbl_group_user_col_user_id__group_id_uq", columnList = "user_id, group_id", unique = true)})
public class GroupUsers {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "group_id")
        private UUID groupId;

        @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private Date createdOn;

        @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private Date modifiedOn;

        @Column(name = "created_by")
        private UUID createdBy;

        @Column(name = "modified_by")
        private UUID modifiedBy;

        @Column(name = "is_deleted")
        private Boolean isDleted;

        @Column(name = "expiry_date")
        private LocalDateTime expiryDate;
}
