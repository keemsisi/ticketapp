package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.core.backend.ticketapp.passport.util.Constants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "module", indexes = {@Index(name = "ix_tbl_module_col_code_uq", columnList = "code", unique = true),
        @Index(name = "ix_tbl_module_col_name_uq", columnList = "normalized_name", unique = true)})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Module {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "code", nullable =  false)
    private String code;

    @Column(name = "normalized_name", nullable = false)
    private String normalizedName;

    @Column(name = "description")
    private String description;

    @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date createdOn = new Date();

    @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date modifiedOn = new Date();

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany
    @JoinColumn(name="module_id", referencedColumnName="id")
    private Set<Action> actions;

    @Transient
    List<String> GenericActions = Constants.GENERAL_ACTIONS;

}
