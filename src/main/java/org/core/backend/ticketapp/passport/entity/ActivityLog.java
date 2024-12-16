package org.core.backend.ticketapp.passport.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "activity_log")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class ActivityLog {
    @Id
    private UUID id;
    @Column(columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateCreated;
    @Column
    private UUID userId;
    @Type(type = "JSONB")
    @Column(columnDefinition = "JSONB")
    private String oldDataModified;
    @Type(type = "JSONB")
    @Column(columnDefinition = "JSONB")
    private String newDataCreated;
    @Column
    private String activityDescription;
    @Column
    private String remoteAddress;
    @Column
    private String httpURI;
    @Column
    private String httpMethod;
    @Column
    private String remoteHost;
    @Column
    private Integer remotePort;
    @Column
    private String entityTypeName;
    @Column
    private UUID tenantId;

    @PrePersist
    void onCreate() {
        this.id = ObjectUtils.defaultIfNull(this.id, UUID.randomUUID());
        this.dateCreated = ObjectUtils.defaultIfNull(this.dateCreated, LocalDateTime.now());
    }
}
