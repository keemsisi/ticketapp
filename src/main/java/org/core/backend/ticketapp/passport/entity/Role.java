package org.core.backend.ticketapp.passport.entity;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@Entity
@Table(name = "role", indexes = {@Index(name = "ix_tbl_role_col_code_uq", columnList = "code", unique = true),
        @Index(name = "ix_tbl_role_col_name_uq", columnList = "normalized_name", unique = true)})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role {

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

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(name = "is_deleted")
    private boolean isDeleted;


    @Transient
    @OneToMany(cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<>();
}