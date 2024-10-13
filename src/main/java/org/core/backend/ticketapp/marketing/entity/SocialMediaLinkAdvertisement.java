package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "social_media_link_advertisement")
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
}
