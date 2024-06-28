package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.GroupActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@Repository
public interface GroupActionRepository extends JpaRepository<GroupActions, UUID>, PagingAndSortingRepository<GroupActions, UUID> {

    @Modifying
    @Query(value = "DELETE FROM group_action WHERE group_id = ?1", nativeQuery = true)
    int deleteByGroupId(UUID groupId);

    @Modifying
    @Query(value = "DELETE FROM group_action WHERE group_id = ?1 and action_id = ?2", nativeQuery = true)
    int deleteByUserIdGroupId(UUID groupId, UUID actionId);
}
