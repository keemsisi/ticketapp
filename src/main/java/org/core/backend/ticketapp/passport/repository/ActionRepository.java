package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface ActionRepository extends JpaRepository<Action, UUID>, PagingAndSortingRepository<Action, UUID> {
        
    @Query(value = "SELECT * FROM action WHERE LOWER(name) = LOWER(?1)", nativeQuery = true)
    Optional<Action> findByName(String name);

    @Query(value = "SELECT * FROM action WHERE module_id = :moduleId ",
            countQuery = "SELECT count(*) FROM action WHERE module_id = :moduleId",
            nativeQuery = true)
    ArrayList<Action> findAllWithoutPaging(UUID moduleId);
}
