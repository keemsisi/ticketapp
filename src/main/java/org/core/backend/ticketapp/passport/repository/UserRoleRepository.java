package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Transactional
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID>, PagingAndSortingRepository<UserRole, UUID> {

    @Query(value = "SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE name = ?1", nativeQuery = true)
    Optional<UserRole> findByValue(String name);

    @Query(value = "SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE ur.id = ?1", nativeQuery = true)
    Optional<UserRole> findById(UUID id);

    @Query(value = "SELECT ur.* FROM user_role ur WHERE ur.id = ?1", nativeQuery = true)
    UserRole findById(UUID id, boolean ll);

    @Query(value = "SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE user_id = ?1 LIMIT 1", nativeQuery = true)
    Optional<UserRole> findByUserId(UUID userId);

    @Query(value = "SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE r.name = ?1 AND ur.user_id = ?2", nativeQuery = true)
    Optional<UserRole> findByValueAndUserId(String name, UUID userId);

    @Query(value = "SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE ur.user_id =?1", nativeQuery = true)
    Set<UserRole> findAllByUserId(UUID userId);

    @Query(value = "SELECT ur.* , r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE ur.user_id = ?1 AND ur.role_id=?2", nativeQuery = true)
    Optional<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);

    @Query(value = "SELECT ur.*, r.name, u.first_name, u.last_name FROM user_role ur LEFT JOIN role r ON r.id = ur.role_id LEFT JOIN users u ON ur.user_id = u.id WHERE ur.role_id = ?1", nativeQuery = true)
    Set<UserRole> findAllByRoleId(UUID roleId);

    @Modifying
    @Query(value = "DELETE FROM user_role WHERE role_id = ?1", nativeQuery = true)
    void deleteByRoleId(UUID roleId);

    @Modifying
    @Query(value = "DELETE FROM user_role WHERE role_id = ?1 and user_id = ?2", nativeQuery = true)
    void deleteUserRoleById(UUID roleId, UUID userId);

    @Query(value = "SELECT ur.*, r.code FROM user_role ur INNER JOIN role r ON r.id = ur.role_id WHERE NOT EXISTS (SELECT * from user_role ur2 WHERE ur2.user_id=?2 AND ur2.role_id = ur.role_id) AND ur.user_id = ?1", nativeQuery = true)
    Set<UserRole> findAllRolesNotExists(UUID userId, UUID reliefOfficer);
}
