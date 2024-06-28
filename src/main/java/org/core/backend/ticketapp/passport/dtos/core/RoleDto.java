package org.core.backend.ticketapp.passport.dtos.core;

import org.core.backend.ticketapp.passport.dtos.user.BriefUserDetail;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class RoleDto {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

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

    @Column(name = "tenant_id")
    private UUID tenantId;

    @Transient
    private List<BriefUserDetail> users = new ArrayList<>();
}