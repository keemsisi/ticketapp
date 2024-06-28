package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface GroupRepository extends JpaRepository<Group, UUID>, PagingAndSortingRepository<Group, UUID> {

    @Query(value = "SELECT * FROM groups WHERE LOWER(name) = LOWER(?1) ", nativeQuery = true)
    Optional<Group> findByName(String name);

    @Query(value = "SELECT * FROM groups WHERE id = ?1", nativeQuery = true)
    Optional<Group> findByUUID(UUID id);

    @Modifying
    @Query(value = "DELETE FROM groups WHERE id = ?1", nativeQuery = true)
    int deleteByUUID(UUID id);

    @Query(value = "SELECT * FROM groups WHERE COALESCE(LOWER(name),'') LIKE CONCAT('%', :name,'%') AND is_deleted = false ",
                nativeQuery = true)
        Page<Group> getAll(String name, Pageable pageable);
}
