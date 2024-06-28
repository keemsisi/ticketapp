package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.ReminderNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReminderNotificationRepository extends JpaRepository<ReminderNotification, UUID>, PagingAndSortingRepository<ReminderNotification, UUID> {
    @NotNull
    Optional<ReminderNotification> findById(@NotNull UUID id);

    @Query(value = "SELECT * FROM reminder_notification WHERE user_id=?1", nativeQuery = true)
    Page<ReminderNotification> findAll(UUID userId, @NotNull Pageable pageable);

    @Query(value = "SELECT * FROM reminder_notification WHERE user_id=?1 AND reminder_date >= CAST(?2 AS TIMESTAMP) AND date_created <= CAST(?3 AS TIMESTAMP)", nativeQuery = true)
    Page<ReminderNotification> getAll(UUID userId, Date expiryDate, Date dateCreated, Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM reminder_notification WHERE user_id=?1", nativeQuery = true)
    Page<ReminderNotification> getAll(UUID userId, Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM reminder_notification WHERE user_id=?1 AND reminder_date = CAST(?2 AS TIMESTAMP)", nativeQuery = true)
    Page<ReminderNotification> getAllByExpiryDate(UUID userId, Date expiryDate, Pageable pageable);

    @NotNull
    @Query(value = "SELECT * FROM reminder_notification WHERE user_id=?1 AND date_created = CAST(?2 AS TIMESTAMP)", nativeQuery = true)
    Page<ReminderNotification> getAllByDateCreated(UUID userId, Date dateCreated, Pageable pageable);

    @Query(value = "SELECT rn.id , rn.active , rn.count , rn.created_by  , rn.date_created , " +
            "rn.modified_on , rn.description , rn.is_deleted , rn.modified_by , " +
            "rn.reminder_date , rn.repeat, rn.title ,rn.user_id, " +
            "u.email as user_email " +
            "FROM reminder_notification rn LEFT JOIN users u ON rn.user_id=u.id " +
            "WHERE rn.reminder_date<= CURRENT_TIMESTAMP AND rn.active=true AND rn.is_deleted=false", nativeQuery = true)
    List<ReminderNotification> getAllExpiredReminderNotifications();
    //CURRENT_TIME <= 2:30AM
}
