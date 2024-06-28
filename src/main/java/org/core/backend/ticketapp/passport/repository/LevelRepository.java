package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Levels;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Transactional
@Repository
public interface LevelRepository extends JpaRepository<Levels, UUID>, PagingAndSortingRepository<Levels, UUID> {

    @Query(value = "SELECT * FROM level WHERE LOWER(name) = LOWER(?1)", nativeQuery = true)
    Optional<Levels> findByName(String name);

    @Query(value = "SELECT * FROM level WHERE id = ?1", nativeQuery = true)
    Optional<Levels> findByUUID(UUID id);

    @Query(value = "SELECT * FROM level WHERE unit_id = ?1 AND Lower(name) = Lower(?2) LIMIT 1", nativeQuery = true)
    Optional<Levels> findByUnitIdAndName(UUID unitId, String name);

    @Query(value = "SELECT * FROM level WHERE name LIKE CONCAT('%', :name,'%') AND " +
            " (unit_id >= :unitId AND unit_id <= :unitIdE) ",
            countQuery = "SELECT count(*) FROM user_unit WHERE name LIKE CONCAT('%', :name,'%') AND " +
                    " (unit_id >= :unitId AND unit_id <= :unitIdE) ",
            nativeQuery = true)
    Page<Levels> findAll(String name, UUID unitId, UUID unitIdE, Pageable pageable);

}
