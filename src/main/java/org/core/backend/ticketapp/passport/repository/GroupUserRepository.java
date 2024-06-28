package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.GroupUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface GroupUserRepository extends JpaRepository<GroupUsers, UUID>, PagingAndSortingRepository<GroupUsers, UUID> {

    @Query(value = "SELECT * FROM group_user WHERE LOWER(name) = LOWER(?1)", nativeQuery = true)
    Optional<GroupUsers> findByName(String name);

    @Query(value = "SELECT gu.id, gu.created_by, gu.created_on, gu.group_id, gu.is_deleted, gu.modified_by, " +
            " gu.modified_on, gu.user_id, gu.expiry_date, u.first_name, u.last_name " +
            " FROM group_user gu LEFT JOIN users u ON gu.user_id = u.id WHERE gu.group_id = ?1", nativeQuery = true)
    List<GroupUsers> findByGroupId(UUID groupId);

    @Query(value = "SELECT COUNT(*) FROM group_user WHERE group_id = ?1", nativeQuery = true)
    long countByGroupId(UUID groupId);

    @Modifying
    @Query(value = "DELETE FROM group_user WHERE group_id = ?1", nativeQuery = true)
    int deleteByGroupId(UUID groupId);

    @Modifying
    @Query(value = "DELETE FROM group_user WHERE group_id = ?1 and user_id = ?2", nativeQuery = true)
    int deleteByUserIdGroupId(UUID groupId, UUID userId);
}
