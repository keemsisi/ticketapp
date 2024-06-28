package org.core.backend.ticketapp.passport.repository;

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
public interface ModuleRepository extends JpaRepository<Module, UUID>, PagingAndSortingRepository<Module, UUID> {

    @Query(value = "SELECT * FROM module ", nativeQuery = true)
    Page<Module> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM module m WHERE m.code=:code", nativeQuery = true)
    Optional<Module> findByModuleCode(String code);
}
