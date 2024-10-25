package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_ads")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class JobAdvertisement extends AbstractBaseEntity {
    @Column(name = "title", columnDefinition = "varchar(250)", nullable = false)
    private String title;
    @Column(name = "description", columnDefinition = "varchar(250)", nullable = false)
    private String description;
    @Column(name = "industry", columnDefinition = "varchar(250)", nullable = false)
    private String industry;
    @Column(name = "address", columnDefinition = "varchar(250)", nullable = false)
    private String address;
    @Column(name = "job_type", columnDefinition = "varchar(250)", nullable = false)
    private String jobType;
    @Column(name = "deadline_date", columnDefinition = "timestamptz", nullable = false)
    private LocalDateTime deadlineDate;
    @Column(name = "cvUrl", columnDefinition = "varchar(250)", nullable = false)
    private String cvUrl;
    @Column(name = "overview", columnDefinition = "varchar(250)", nullable = false)
    private String overview;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}
