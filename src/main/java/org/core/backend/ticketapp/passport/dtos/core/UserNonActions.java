package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
public class UserNonActions {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "action_id")
        private UUID actionId;

        @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private DateTime createdOn;

        @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        private DateTime modifiedOn;

        @Column(name = "created_by")
        private String createdBy;

        @Column(name = "modified_by")
        private String modifiedBy;

        @Column(name = "is_deleted")
        private int isDeleted;
}
