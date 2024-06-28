package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "system_alert", indexes = {@Index(name = "ix_tbl_tenant_col_tenant_uq", columnList = "tenant_id", unique = true)})
public class SystemAlert {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "email_alert")
    private boolean emailAlert;

    @Column(name = "sms_alert")
    private boolean smsAlert;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_created", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private LocalDateTime dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_modified", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateModified = null;

    @Column(name = "created_by" , nullable = false)
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;
}
