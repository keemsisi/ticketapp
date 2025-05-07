package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.SocialMedia;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "social_media_link_advertisement", indexes = {})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class SocialMediaLinkAdvertisement extends AbstractBaseEntity {
    @Column(name = "profile_link", columnDefinition = "varchar(500)")
    private String profileLink;

    @Column(name = "handle", columnDefinition = "varchar(100)")
    private String handle;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "social_media_type", columnDefinition = "varchar(100)")
    private SocialMedia socialMediaType;


    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}

