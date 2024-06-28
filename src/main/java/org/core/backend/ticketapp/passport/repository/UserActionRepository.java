package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Transactional
@Repository
public interface UserActionRepository extends JpaRepository<UserAction, UUID>, PagingAndSortingRepository<UserAction, UUID> {

    @Query(value = "SELECT up.id as id, action_id, user_id, p.code as code FROM user_action up INNER JOIN action p ON p.id = up.action_id WHERE code = ?1", nativeQuery = true)
    Optional<UserAction> findByValue(String value);

    @Query(value = "SELECT up.id as id, action_id, user_id, p.code as code FROM user_action up INNER JOIN action p ON p.id = up.action_id WHERE up.id = ?1", nativeQuery = true)
    Optional<UserAction> findById(UUID id);

    @Query(value = "SELECT up.*, p.code as code FROM user_action up INNER JOIN action p ON p.id = up.action_id WHERE up.user_id = ?1 and up.action_id = ?2", nativeQuery = true)
    Optional<UserAction> findByUserIdAndActionId(UUID userId, UUID actionId);

    @Query(value = "SELECT up.id as id, action_id, user_id, p.code as code FROM user_action up INNER JOIN action p ON p.id = up.action_id WHERE code = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<UserAction> findByValueAndUserId(String code, UUID userId);

    @Query(value = "SELECT up.*, p.code FROM user_action up LEFT JOIN action p ON p.id = up.action_id WHERE user_id = ?1", nativeQuery = true)
    Optional<Set<UserAction>> findAllByUserId(UUID userId);

    @Query(value = "SELECT up.*, p.code FROM user_action up LEFT JOIN action p ON p.id = up.action_id WHERE NOT EXISTS (SELECT * from user_action ua WHERE ua.user_id=?2 AND ua.action_id = up.action_id) AND user_id = ?1", nativeQuery = true)
    Optional<Set<UserAction>> findAllActionsNotExists(UUID userId, UUID reliefOfficer);

}
