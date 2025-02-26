package org.core.backend.ticketapp.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@OptimisticLocking(type = OptimisticLockType.VERSION)
public abstract class AbstractBaseEntity {
    @Id
    @Column(columnDefinition = "UUID DEFAULT uuid_generate_v4() NOT NULL")
    protected UUID id;

    @CreationTimestamp
    @Column(name = "date_created", columnDefinition = "timestamptz DEFAULT CURRENT_DATE NOT NULL", updatable = false)
    protected LocalDateTime dateCreated;

    @Column(updatable = false, name = "user_id")
    protected UUID userId;

    @Column(columnDefinition = "timestamptz")
    protected LocalDateTime dateModified;

    protected UUID modifiedBy;

    @NotNull
    @Column(columnDefinition = "SERIAL UNIQUE", insertable = false, updatable = false)
    protected long index;

    @NotNull
    @Column(columnDefinition = "bool default false")
    protected boolean deleted;

    @JsonIgnore
    @Column(columnDefinition = "bigint default(0)")
    private long version;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Column(name = "tenant_id", columnDefinition = "uuid default null")
    private UUID tenantId;

    @Version
    private Long versionId;

    @JsonIgnore
    @Transient
    private String qrCodeLink;

}
