package org.core.backend.ticketapp.marketing_ads.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.marketing_ads.repository.SocialMediaLinkAdvertisementRepository;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "social_media_link_ad")
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class SocialMediaLinkAdvertisement extends AbstractBaseEntity {
    private final SocialMediaLinkAdvertisementRepository repository;
}
