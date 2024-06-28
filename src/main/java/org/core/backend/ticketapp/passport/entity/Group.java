package org.core.backend.ticketapp.passport.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "groups", indexes = {@Index(name = "ix_tbl_group_col_code_uq", columnList = "code", unique = true),
        @Index(name = "ix_tbl_group_col_name_uq", columnList = "normalized_name", unique = true)})
public class Group {
        @Id
        @Column(name = "id")
        private UUID id;

        @Column(name = "name")
        private String name;

        @Column(name = "normalized_name", nullable = false)
        private String normalizedName;

        @Column(name = "code", nullable = false)
        private String code;

        @Column(name = "description")
        private String description;

        @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
        @Temporal(TemporalType.TIMESTAMP)
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
        private List<GroupActions> groupActions = new ArrayList();

        @Transient
        private List<GroupUsers> groupUsers = new ArrayList();
}
