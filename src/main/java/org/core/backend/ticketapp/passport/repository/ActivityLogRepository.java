package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID>, PagingAndSortingRepository<ActivityLog, UUID> {
    @Query(value = "SELECT * FROM activity_log WHERE user_id=?1", nativeQuery = true)
    Page<ActivityLog> getUserActivities(UUID userId, Pageable pageable);
    @Query(value = "SELECT * FROM activity_log WHERE user_id=?1 ORDER BY date_created DESC", nativeQuery = true)
    List<ActivityLog> getUserActivities(UUID userId);
}
