package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.BlobType;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_subscriber")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@TypeDefs({
        @TypeDef(name = "JSON", typeClass = JsonStringType.class),
        @TypeDef(name = "JSONB", typeClass = JsonBinaryType.class),
        @TypeDef(name = "BLOB", typeClass = BlobType.class),
        @TypeDef(name = "LIST-ARRAY", typeClass = ListArrayType.class)
})
public class NotificationSubscriber {
    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_seen", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime lastSeen;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID createdBy;

    @Column(name = "active")
    private boolean active;

    @Column(name = "token", nullable = false, columnDefinition = "text")
    private String token;

    @Type(type = "JSONB")
    @Column(name = "user_data", columnDefinition = "JSONB DEFAULT null")
    private String userData;

    @Column(name = "unsubscribed", columnDefinition = "boolean")
    private boolean unSubscribed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_unsubscribed", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateUnSubscribed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_subscribed", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private LocalDateTime dateSubscribed;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_created", columnDefinition = "TIMESTAMPTZ default CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime dateCreated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_modified", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateModified;

    @Column(name = "last_seen_session_id")
    private UUID lastSeenSessionId;

    @Column(name = "last_seen_remote_address")
    private String lastSeenRemoteAddress;

    @Column(nullable = false, name = "current_session_id")
    private UUID currentSessionId;

    @Column(nullable = false, name = "current_seen_remote_address")
    private String currentSeenRemoteAddress;

    @Type(type = "LIST-ARRAY")
    @Column(nullable = false, name = "subscriber_scope", columnDefinition = "text[]")
    private List<String> subscriberScope = Collections.emptyList();
}
