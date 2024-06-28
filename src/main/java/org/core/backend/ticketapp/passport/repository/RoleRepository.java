package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public interface RoleRepository extends JpaRepository<Role, UUID>, PagingAndSortingRepository<Role, UUID> {

    @Query(value = "SELECT * FROM role WHERE LOWER(name) = LOWER(?1) ", nativeQuery = true)
    Optional<Role> getByName(String name);

    @Query(value = "SELECT * FROM role WHERE id = ?1 ", nativeQuery = true)
    Optional<Role> getByUUID(UUID id);

    @Query(value = "SELECT * FROM role WHERE code = ?1 ", nativeQuery = true)
    List<Role> getByCode(String code);

    @Query(value = "SELECT * FROM role WHERE COALESCE(LOWER(name),'') LIKE CONCAT('%', ?1,'%') AND is_deleted = false ", nativeQuery = true)
    Page<Role> getAll(String name, Pageable pageable);

    @Query(value = "SELECT * FROM role WHERE is_deleted = false", nativeQuery = true)
    Page<Role> getAll(Pageable pageable);
}
