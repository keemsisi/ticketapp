package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.ReadNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface ReadNotificationRepository extends JpaRepository<ReadNotification, UUID>, PagingAndSortingRepository<ReadNotification, UUID> {
    @Query(value = "SELECT * FROM notification n WHERE NOT EXISTS (SELECT 1 FROM read_notification rn  WHERE rn.user_id=?1 AND rn.notification_id=n.id)", nativeQuery = true)
    List<Notification> getUserUnreadNotification(UUID userId);

    @Modifying
    @Query(value = "INSERT INTO read_notification(user_id , notification_id) VALUES(?1, ?2);", nativeQuery = true)
    Optional<Notification> insertReadNotification(UUID userId, UUID notificationId);

    @Query(value = "SELECT * FROM read_notification rn WHERE rn.user_id=?1", nativeQuery = true)
    Optional<Notification> getUserReadNotification(UUID userId);
}
