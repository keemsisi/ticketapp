package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
public class Permission {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "value")
    private String value;

    @Column(name = "module")
    private String module;

    @Column(name = "description")
    private String description;

    @Column(name = "sub_module_id")
    private UUID subModuleId;

    @Transient
    private Boolean assigned = false;
}
