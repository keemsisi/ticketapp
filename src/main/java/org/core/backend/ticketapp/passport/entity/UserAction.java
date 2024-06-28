package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_action", indexes = {@Index(name = "ix_tbl_user_action_col_user_id__action_id_uq", columnList = "user_id, action_id", unique = true)})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAction {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "user_id", nullable = false)
        private UUID userId;

        @Column(name = "action_id", nullable = false)
        private UUID actionId;

        @Transient
        private String code;

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

        @Column(name = "expiry_date")
        private LocalDateTime expiryDate;

        @Transient
        private String firstName;

        @Transient
        private String lastName;

        @Transient
        private String middleName;

        @Transient
        private String userFullName;

        public String getUserFullName() {
                return firstName + " " + (ObjectUtils.isEmpty(middleName) ? "" : middleName + " ") + lastName;
        }
}
