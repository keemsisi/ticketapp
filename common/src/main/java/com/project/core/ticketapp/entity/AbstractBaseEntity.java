package com.project.core.ticketapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class AbstractBaseEntity {
    @Id
    @Column(columnDefinition = "UUID NOT NULL default uuid_generate_v1()")
    protected UUID id;
    @Column(columnDefinition = "bigint default(0)")
    private long version;
    @CreationTimestamp
    @Column(columnDefinition = "timestamp with time zone DEFAULT CURRENT_DATE NOT NULL", updatable = false)
    protected LocalDateTime dateCreated;
    @Column(updatable = false)
    protected UUID createdBy;
    @UpdateTimestamp
    @Column(columnDefinition = "timestamp with time zone")
    protected LocalDateTime dateModified;
    protected UUID modifiedBy;
    @NotNull
    @Column(columnDefinition = "SERIAL UNIQUE", insertable = false, updatable = false)
    protected long index;
    @NotNull
    @Column(columnDefinition = "bool default false")
    protected boolean deleted;
    @Column(columnDefinition = "bool default false")
    protected boolean active;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        if (dateCreated == null) LocalDateTime.now();

    }
}
