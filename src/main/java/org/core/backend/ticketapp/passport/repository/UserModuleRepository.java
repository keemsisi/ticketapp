package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.UserModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserModuleRepository extends JpaRepository<UserModule, UUID>, PagingAndSortingRepository<UserModule, UUID> {

    @Query(value = "SELECT um.id as id, module_id, user_id, r.* FROM user_module um INNER JOIN module r ON r.id = um.module_id WHERE name = ?1", nativeQuery = true)
    Optional<UserModule> findByValue(String name);

    @Query(value = "SELECT um.id as id, module_id, user_id, r.* FROM user_module um INNER JOIN module r ON r.id = um.module_id WHERE um.id = ?1", nativeQuery = true)
    Optional<UserModule> findById(UUID id);

    @Query(value = "SELECT um.id as id, module_id, user_id, r.* FROM user_module um INNER JOIN module r ON r.id = um.module_id WHERE user_id = ?1 LIMIT 1", nativeQuery = true)
    Optional<List<UserModule>> findByUserId(UUID userId);

    @Query(value = "SELECT um.id as id, module_id, user_id, r.* FROM user_module um INNER JOIN module r ON r.id = um.module_id WHERE name = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<UserModule> findByValueAndUserId(String name, UUID userId);

    @Query(value = "SELECT um.id as id, um.module_id, um.user_id, r.* FROM user_module um INNER JOIN module r ON r.id = um.module_id WHERE um.user_id = ?1", nativeQuery = true)
    Set<UserModule> findAllByUserId(UUID userId);

    @Query(value = "SELECT um.id as id, um.module_id, um.user_id, m.* FROM user_module um INNER JOIN module m ON m.id = um.module_id WHERE um.module_id = ?1", nativeQuery = true)
    Set<UserModule> findByModuleId(UUID moduleId);

    @Query(value = "SELECT um.*, um.module_id, um.user_id, r.name, u.first_name, u.last_name FROM user_module um LEFT JOIN module r ON r.id = um.module_id LEFT JOIN users u ON um.user_id = u.id WHERE um.module_id = ?1", nativeQuery = true)
    Set<UserModule> findUserModuleByModuleId(UUID moduleId);

    @Modifying
    @Query(value = "DELETE FROM user_module WHERE module_id = ?1", nativeQuery = true)
    void deleteByModuleId(UUID moduleId);

}
