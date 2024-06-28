package org.core.backend.ticketapp.passport.repository;


import org.core.backend.ticketapp.passport.entity.WebSocketPushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WebSocketInAppNotificationRepository extends JpaRepository<WebSocketPushNotification, UUID> {

    @Query(value = "SELECT * FROM web_socket_push_notification win WHERE win.notification_id = ?1 " +
            " AND win.user_id = ?2 ", nativeQuery = true)
    Optional<WebSocketPushNotification> findByNotificationIdAndUserId(UUID notificationId, UUID userId);
}
