package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.FileTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Transactional
@Repository
public interface FileTemplateRepository extends JpaRepository<FileTemplate, UUID>, PagingAndSortingRepository<FileTemplate, UUID> {

    @Query(value = "SELECT * FROM file_template WHERE name = ?1", nativeQuery = true)
    Optional<FileTemplate> findByName(String name);
}
