package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "relief_request")
@Entity
@TypeDefs({
        @TypeDef(name = "JSONB", typeClass = JsonBinaryType.class),
        @TypeDef(name = "workflow_approval_status_enum", typeClass = PostgreSQLEnumType.class)
})
public class ReliefRequest {
    @Id
    private UUID id;
    @NotNull
    @Column
    private UUID reliefOfficerId;
    @Column
    private UUID requestedById;
    @NotNull
    @Column
    private LocalDateTime startDate;
    @NotNull
    @Column
    private LocalDateTime endDate;
    @Column
    private String remark;
    @Type(type = "workflow_approval_status_enum")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "workflow_approval_status_enum")
    private ApprovalStatus approvalStatus;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_created", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateCreated;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_modified", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateModified;
    @Column
    private UUID modifiedBy;
    @Type(type = "JSONB")
    @Column(columnDefinition = "jsonb") //{roles: [] , actions: []}
    private String roleAndActionInherited;
    @NotNull
    @Column
    private UUID tenantId;
    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "relief_officer_name")
    private String reliefOfficerName;
}
