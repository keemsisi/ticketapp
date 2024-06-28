package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.ActionWorkflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface ActionWorkflowRepository extends JpaRepository<ActionWorkflow, UUID>, PagingAndSortingRepository<ActionWorkflow, UUID> {


    @Query(value = "SELECT workflow.* FROM workflow LEFT JOIN action on workflow.action_id = action.id WHERE action.code = LOWER(?1) AND workflow.is_deleted=false", nativeQuery = true)
    Optional<ActionWorkflow> findByActionCode(String actionName);

    @Query(value = "SELECT * FROM workflow WHERE LOWER(code) = LOWER(?1)", nativeQuery = true)
    Optional<ActionWorkflow> findByCode(String code);

    @Query(value = "SELECT * FROM workflow WHERE id = ?1", nativeQuery = true)
    Optional<ActionWorkflow> findByUUID(UUID id);

    @Query(value = "SELECT * FROM workflow WHERE module_id = ?1", nativeQuery = true)
    List<ActionWorkflow> findByModuleId(UUID id);

    @Modifying
    @Query(value = "DELETE FROM workflow WHERE id = ?1", nativeQuery = true)
    void deleteByUUID(UUID id);

    @Query(value = "SELECT * FROM workflow WHERE COALESCE(LOWER(name),'') LIKE CONCAT('%', :name,'%') AND is_deleted = false",
            nativeQuery = true)
    Page<ActionWorkflow> getAll(String name, Pageable pageable);

}
