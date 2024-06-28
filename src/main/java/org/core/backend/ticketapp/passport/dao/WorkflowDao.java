package org.core.backend.ticketapp.passport.dao;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.dtos.core.ApprovalLevel;
import org.core.backend.ticketapp.passport.entity.ActionWorkflow;
import org.core.backend.ticketapp.passport.entity.Workflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
public class WorkflowDao extends BaseDao {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    public List<ActionWorkflow> getActionsByWorkflowId(UUID workflowId) {
        String sql = "SELECT aw.*, a.code, a.name FROM action_workflow aw LEFT JOIN action a ON a.id = aw.action_id WHERE aw.workflow_id = ?";
        var rowMapper = BeanPropertyRowMapper.newInstance(ActionWorkflow.class);
        return getJdbcTemplate().query(sql, rowMapper, workflowId);
    }

    public Optional<Workflow> getWorkflowByActionCode(String actionCode) {
        String sql = "SELECT w.*, a.code FROM workflow w LEFT JOIN action_workflow aw on aw.workflow_id = w.id LEFT JOIN action a on a.id = aw.action_id WHERE a.code = LOWER(?) AND w.is_deleted=false";
        var rowMapper = BeanPropertyRowMapper.newInstance(Workflow.class);
        return Optional.ofNullable(getJdbcTemplate().query(sql, rowMapper, actionCode).get(0));
    }

    public List<ApprovalLevel> getWorkflowApprovalLevelByActionCode(String actionCode) {
        String sql = "SELECT aw.workflow_id, wl.id as level_id,  wl.role_id, wl.level_no, r.code as role_code, 'PENDING' as approval_status from workflow_level  wl LEFT JOIN action_workflow aw on aw.workflow_id = wl.workflow_id LEFT JOIN action a on a.id = aw.action_id  left join role r on r.id  = wl.role_id WHERE a.code = ? AND a.is_deleted=false";
        var rowMapper = BeanPropertyRowMapper.newInstance(ApprovalLevel.class);
        return getJdbcTemplate().query(sql, rowMapper, actionCode);
    }

    @Modifying
    public void deleteActionsByWorkflowIdAndActionId(List<UUID> actionIds, UUID workflowId) {
        String inSql = String.join(",", Collections.nCopies(actionIds.size(), "?"));
        getJdbcTemplate().update(String.format("DELETE FROM action_workflow WHERE workflow_id = '%s' AND action_id IN (%s)", workflowId, inSql), actionIds.toArray());
    }

    public List<ActionWorkflow> getActionWorkflowByWorkflowIdAndActionIds(List<UUID> actionIds, UUID workflowId) {
        String inSql = String.join(",", Collections.nCopies(actionIds.size(), "?"));
        var rowMapper = BeanPropertyRowMapper.newInstance(ActionWorkflow.class);
        return getJdbcTemplate().query(String.format("SELECT aw.*, a.code, a.name FROM action_workflow aw LEFT JOIN action a ON a.id = aw.action_id WHERE aw.workflow_id = '%s' AND action_id IN (%s)", workflowId, inSql), rowMapper, actionIds.toArray());
    }

    @Modifying
    public void deleteActionsByWorkflowId(UUID workflowId) {
        getJdbcTemplate().update("DELETE FROM action_workflow WHERE workflow_id = ?", workflowId);
    }
}
