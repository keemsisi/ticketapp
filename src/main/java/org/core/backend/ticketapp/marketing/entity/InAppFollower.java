package org.core.backend.ticketapp.marketing.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.marketing.common.FollowerStatus;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "in_app_follower", indexes = {
        @Index(name = "in_app_follower_user_id_idx", columnList = "user_id"),
        @Index(name = "in_app_follower_user_id_followed_user_id_idx", columnList = "user_id, followed_user_id"),
        @Index(name = "in_app_follower_followed_user_id_status_idx", columnList = "followed_user_id, status")
})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class)})
public class InAppFollower extends AbstractBaseEntity {
    // userId ->> following followedUserId
    @Column(name = "followed_user_id")
    private UUID followedUserId;
    @Column(columnDefinition = "bool default true")
    private boolean alertOnPost;
    @Column(columnDefinition = "bool default true")
    private boolean alertOnEvent;
    @Column(columnDefinition = "bool default true")
    private boolean receiveSmsAlert;
    @Column(columnDefinition = "bool default true")
    private boolean receiveEmailAlert;
    @Column(columnDefinition = "bool default true")
    private boolean receiveInAppAlert;
    @Column(columnDefinition = "bool default true")
    private boolean alertOnAnyActivity;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(255) default 'FOLLOWED'")
    private FollowerStatus status;
}
