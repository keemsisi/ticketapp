package org.core.backend.ticketapp.passport.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_module", indexes = {@Index(name = "ix_tbl_user_module_col_user_id__module_id_uq", columnList = "user_id, module_id", unique = true)})
public class UserModule {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "user_id", nullable = false)
        private UUID userId;

        @Column(name = "module_id", nullable = false)
        private UUID moduleId;

        @Transient
        private String code;

        @Transient
        private String name;

        @Transient
        private String description;

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
