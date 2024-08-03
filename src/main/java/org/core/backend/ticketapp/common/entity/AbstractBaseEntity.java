package org.core.backend.ticketapp.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractBaseEntity {
    @Id
    @Column(columnDefinition = "UUID NOT NULL default uuid_generate_v1()")
    protected UUID id;
    @Column(columnDefinition = "timestamptz with time zone DEFAULT CURRENT_DATE NOT NULL", updatable = false)
    protected LocalDateTime dateCreated;
    @Column(updatable = false, name = "user_id")
    protected UUID userId;
    @Column(columnDefinition = "timestamptz with time zone")
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
    @Column(name = "type")
    private String type;
}
