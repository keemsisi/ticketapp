package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface NotificationSubscriberRepository extends JpaRepository<NotificationSubscriber, UUID>, PagingAndSortingRepository<NotificationSubscriber, UUID> {
    @Query(value = "SELECT * FROM notification_subscriber ns WHERE ns.user_id=?1", nativeQuery = true)
    Optional<NotificationSubscriber> findByUserId(UUID userId);

    @Query(value = "SELECT * FROM notification_subscriber ns WHERE ns.current_session_id=?1", nativeQuery = true)
    Optional<NotificationSubscriber> findByCurrentSessionId(UUID sessionId);

    @Query(value = "SELECT * FROM notification_subscriber ns WHERE ns.user_id=?1 AND unsubscribed=?2 AND active=?3", nativeQuery = true)
    Optional<NotificationSubscriber> findByUserIdAndNotUnsubscribedAndActive(UUID userId, boolean unsubscribed, boolean active);

    @Query(value = "SELECT * FROM notification_subscriber ns WHERE ns.user_id IN ?1 AND unsubscribed=?2 AND active=?3", nativeQuery = true)
    Optional<List<NotificationSubscriber>> findByUserIdAndNotUnsubscribedAndActive(List<UUID> userIds, boolean unsubscribed, boolean active);
}
