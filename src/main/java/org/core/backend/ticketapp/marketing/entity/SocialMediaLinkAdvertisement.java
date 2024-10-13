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
@Table(name = "social_media_link_advertisement", indexes = {})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class SocialMediaLinkAdvertisement extends AbstractBaseEntity {
    @Column(name = "ticket_global", columnDefinition = "varchar(500)")
    private String ticketGlobal;
    @Column(name = "instagram", columnDefinition = "varchar(500)")
    private String instagram;
    @Column(name = "tiktok", columnDefinition = "varchar(500)")
    private String tiktok;
    @Column(name = "twitter", columnDefinition = "varchar(500)")
    private String twitter;
    @Column(name = "facebook", columnDefinition = "varchar(500)")
    private String facebook;
    @Column(name = "linkedin", columnDefinition = "varchar(500)")
    private String linkedIn;
    @Column(name = "whatsapp", columnDefinition = "varchar(500)")
    private String whatsApp;

    @PrePersist
    public void onCreate() {
        id = ObjectUtils.defaultIfNull(id, UUID.randomUUID());
        dateCreated = ObjectUtils.defaultIfNull(dateCreated, LocalDateTime.now());
    }
}

