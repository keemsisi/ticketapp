package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.Workflow;
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
public interface WorkflowRepository extends JpaRepository<Workflow, UUID>, PagingAndSortingRepository<Workflow, UUID> {

    @Query(value = "SELECT * FROM workflow WHERE name =LOWER(?1)", nativeQuery = true)
    Optional<Workflow> findByName(String name);

    @Query(value = "SELECT workflow.* FROM workflow LEFT JOIN action on workflow.action_id = action.id WHERE action.code = LOWER(?1) AND workflow.is_deleted=false", nativeQuery = true)
    Optional<Workflow> findByActionCode(String actionName);

    @Query(value = "SELECT * FROM workflow WHERE LOWER(code) = LOWER(?1)", nativeQuery = true)
    Optional<Workflow> findByCode(String code);

    @Query(value = "SELECT * FROM workflow WHERE id = ?1", nativeQuery = true)
    Optional<Workflow> findByUUID(UUID id);

    @Query(value = "SELECT * FROM workflow WHERE module_id = ?1 AND tenant_id=?2", nativeQuery = true)
    List<Workflow> findByModuleId(UUID id, UUID tenantId);

    @Query(value = "SELECT * FROM workflow WHERE LOWER(name) LIKE CONCAT('%', LOWER(?2),'%') AND module_id = ?1 AND tenant_id=?3", nativeQuery = true)
    List<Workflow> findByModuleIdAndName(UUID id,String name, UUID tenantId);

    @Modifying
    @Query(value = "DELETE FROM workflow WHERE id = ?1", nativeQuery = true)
    void deleteByUUID(UUID id);

    @Query(value = "SELECT * FROM workflow WHERE COALESCE(LOWER(name),'') LIKE CONCAT('%', :name,'%') AND is_deleted = false AND tenant_id=:tenantId",
            nativeQuery = true)
    Page<Workflow> getAllByName(String name, UUID tenantId, Pageable pageable);

}
