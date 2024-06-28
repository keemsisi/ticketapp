package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.WorkflowLevels;
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
public interface WorkflowLevelRepository extends JpaRepository<WorkflowLevels, UUID>, PagingAndSortingRepository<WorkflowLevels, UUID> {

    @Query(value = "SELECT * FROM workflow_level WHERE LOWER(name) = LOWER(?1)", nativeQuery = true)
    Optional<WorkflowLevels> findByName(String name);

    @Query(value = "SELECT * FROM workflow_level WHERE workflow_id = ?1", nativeQuery = true)
    List<WorkflowLevels> findByWorkflowId(UUID workflowId);

    @Query(value = "SELECT COUNT(*) FROM workflow_level WHERE workflow_id = ?1", nativeQuery = true)
    long countByWorkflowId(UUID workflowId);

    @Query(value = "SELECT * FROM workflow_level WHERE role_id = ?1", nativeQuery = true)
    List<WorkflowLevels> findByRoleId(UUID roleId);

    @Modifying
    @Query(value = "DELETE FROM workflow_level WHERE workflow_id = ?1", nativeQuery = true)
    int deleteByWorkflowId(UUID workflowId);

    @Modifying
    @Query(value = "DELETE FROM workflow_level WHERE id = ?1", nativeQuery = true)
    void deleteById(UUID levelId);

//    @Modifying
//    @Query(value = "INSERT INTO workflow_level(id, created_by, is_deleted, name, normalized_name, role_id, workflow_id, level_no, description, approval_status) VALUES(?1, ?2, false, ?3, ?4, ?5, ?6, ?7, ?8, ?9", nativeQuery = true)
//    void save(UUID id, UUID createdBy, String name, String normalizedName, UUID roleId, UUID workflowId, Integer levelNo, String description, String approvalStatus);
}
