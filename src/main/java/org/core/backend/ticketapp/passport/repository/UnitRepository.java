package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Unit;
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
public interface UnitRepository extends JpaRepository<Unit, UUID>, PagingAndSortingRepository<Unit, UUID> {

    @Query(value = "SELECT * FROM unit WHERE Lower(name) = ?1", nativeQuery = true)
    Optional<Unit> findByName(String name);

    @Query(value = "SELECT * FROM unit WHERE id = ?1", nativeQuery = true)
    Optional<Unit> findByUUID(UUID id);

    @Query(value = "SELECT * FROM unit WHERE department_id = ?1 AND Lower(name) = Lower(?2) LIMIT 1", nativeQuery = true)
    Optional<Unit> findByDepartmentIdAndName(UUID departmentId, String name);

    @Query(value = "SELECT * FROM unit WHERE name LIKE CONCAT('%', :name,'%') AND " +
            " department_id = :departmentId ", nativeQuery = true)
    Page<Unit> findAll(String name, UUID departmentId, Pageable pageable);

}
