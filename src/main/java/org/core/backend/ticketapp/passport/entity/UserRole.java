package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "user_role", indexes = {@Index(name = "ix_tbl_user_role_col_user_id__role_id_uq", columnList = "user_id, role_id", unique = true)})
public class UserRole implements Serializable {

    @Id
    @Column(name = "id")
    private UUID id;

    @JsonIgnore
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    private Date modifiedOn;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "code", insertable = false, updatable = false)
    @Transient
    private String code;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
}
