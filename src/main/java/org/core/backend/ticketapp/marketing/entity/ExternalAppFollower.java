package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.marketing.common.FollowerStatus;
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
@Table(name = "external_app_follower", indexes = {
        @Index(name = "in_app_followed_user_id_idx", columnList = "user_id"),
        @Index(name = "in_app_follower_user_id_social_media_link_ad_id_idx", columnList = "follower_user_id, social_media_link_ad_id", unique = true),
        @Index(name = "in_app_follower_user_id_status_idx", columnList = "follower_user_id, status")
})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class ExternalAppFollower extends AbstractBaseEntity {
    @Column(name = "social_media_link_ad_id", nullable = false)
    private UUID socialMediaLinkAdId;

    @Column(name = "follower_user_id", nullable = false)
    private UUID followerUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(255) default 'FOLLOWED'")
    private FollowerStatus status;

    @PrePersist
    public void onCreate() {
        this.id = ObjectUtils.defaultIfNull(this.id, UUID.randomUUID());
        this.dateCreated = ObjectUtils.defaultIfNull(this.dateCreated, LocalDateTime.now());
    }
}
