package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_ads_application", indexes = {
        @Index(name = "ix_job_ads_application__job_id_email__uq",
        columnList = "job_id, email", unique = true)
})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class JobAdvertisementApplication extends AbstractBaseEntity {

    @Column(name = "job_id", columnDefinition = "UUID NOT NULL")
    @NotNull(message = "jobId can't be blank")
    private UUID jobId;

    @NotNull(message = "jobId can't be blank")
    @Email(message = "valid email address is required")
    private String email;

    @Column(name = "full_name", columnDefinition = "varchar(250)", nullable = false)
    private String fullName;

    @Column(name = "resume_url", columnDefinition = "varchar(5000)", nullable = false)
    private String resumeUrl;

    @Column(name = "cover_letter", columnDefinition = "varchar(10000)", nullable = false)
    private String coverLetter;

    @Column(name = "linked_in_profile", columnDefinition = "varchar(250)", nullable = false)
    private String linkedinProfile;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}
