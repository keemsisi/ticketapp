package org.core.backend.ticketapp.passport.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@Entity
@Table(name = "web_socket_push_notification")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@TypeDefs({@TypeDef(name = "JSON", typeClass = JsonStringType.class)})
public class WebSocketPushNotification {
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "notification_id", nullable = false)
    private UUID notificationId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "notification_delivered", nullable = false)
    private boolean notificationDelivered;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_processed")
    private LocalDateTime dateDelivered;

    @Column(name = "date_retry_sent")
    private LocalDateTime dateLastSent;

    @Column(name = "retry_count", columnDefinition = "integer")
    private int retryCount;

    @Column(name = "session_id",nullable = false)
    private UUID sessionId;

    @PrePersist
    private void onCreate() {
        if (dateCreated == null)
            dateCreated = LocalDateTime.now();
        if (id == null) id = UUID.randomUUID();
    }
}
