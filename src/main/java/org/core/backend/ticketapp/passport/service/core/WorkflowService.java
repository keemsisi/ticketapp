package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.dao.WorkflowDao;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.notification.CreateWorkflowDTO;
import org.core.backend.ticketapp.passport.entity.ActionWorkflow;
import org.core.backend.ticketapp.passport.entity.Workflow;
import org.core.backend.ticketapp.passport.entity.WorkflowLevels;
import org.core.backend.ticketapp.passport.repository.ActionWorkflowRepository;
import org.core.backend.ticketapp.passport.repository.WorkflowLevelRepository;
import org.core.backend.ticketapp.passport.repository.WorkflowRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ObjectUtils;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class WorkflowService extends BaseRepoService<Workflow> {

    @Autowired
    private WorkflowRepository repository;

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private ActionWorkflowRepository actionWorkflowRepository;

    @Autowired
    private WorkflowLevelRepository workflowLevelRepository;

    @Autowired
    private WorkflowLevelService workflowLevelService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public WorkflowService(WorkflowRepository repository) {
        super(repository);
    }

    @Transactional
    public void createWorkflow(CreateWorkflowDTO createWorkflowDTO, LoggedInUserDto user) {
        Workflow workflow = new Workflow();
        BeanUtils.copyProperties(createWorkflowDTO, workflow, ObjectUtils.getNullPropertyNames(createWorkflowDTO));
        workflow.setId(UUID.randomUUID());
        workflow.setCreatedOn(DateTime.now().toDate());
        workflow.setActive(true);
        workflow.setCreatedBy(user.getUserId());
        workflow.setName(createWorkflowDTO.getName());
        workflow.setNormalizedName(StringUtil.normalizeString(workflow.getName()));
        Workflow response = repository.saveAndFlush(workflow);

        workflowLevelService.create(createWorkflowDTO.getLevels(), response.getId(), user);
        createWorkflowActions(createWorkflowDTO.getActionId(), response.getId(), user);
    }

    @Transactional
    public List<ActionWorkflow> createWorkflowActions(List<UUID> actionIds, UUID workflowId, LoggedInUserDto user) {
        var actionWorkflows = actionIds.stream().map(actionId -> {
            ActionWorkflow actionWorkflow = new ActionWorkflow();
            actionWorkflow.setId(UUID.randomUUID());
            actionWorkflow.setCreatedOn(DateTime.now().toDate());
            actionWorkflow.setCreatedBy(user.getUserId());
            actionWorkflow.setActionId(actionId);
            actionWorkflow.setWorkflowId(workflowId);
            return actionWorkflow;
        }).collect(Collectors.toList());

        return actionWorkflowRepository.saveAllAndFlush(actionWorkflows);
    }

    public void deleteAllActionsByWorkflowId(UUID workflowId) {
        workflowDao.deleteActionsByWorkflowId(workflowId);
    }

    public List<ActionWorkflow> deleteActionsByWorkflowId(List<UUID> actionIds, UUID workflowId) {
        List<ActionWorkflow> actionWorkflows = workflowDao.getActionWorkflowByWorkflowIdAndActionIds(actionIds, workflowId);
        actionWorkflowRepository.deleteAll(actionWorkflows);
        return actionWorkflows;
//        workflowDao.deleteActionsByWorkflowIdAndActionId(actionIds, workflowId);
    }

    public Optional<Workflow> getByName(String name) {
        return repository.findByName(name);
    }

    public List<ActionWorkflow> getWorkflowActionById(UUID id) {
        return workflowDao.getActionsByWorkflowId(id);
    }

    public Optional<Workflow> getByUUID(UUID id) {
        return repository.findByUUID(id);
    }

    public void deleteByUUID(UUID id) {
        repository.deleteByUUID(id);
    }

    public void deleteWorkflowActionsByUUID(List<UUID> actionIds, UUID workflowId) {
        workflowDao.deleteActionsByWorkflowIdAndActionId(actionIds, workflowId);
    }

    public Optional<Workflow> findById(UUID workflowId) {
        Optional<Workflow> workflow = repository.findById(workflowId);

        return workflow;
    }

    public Page<Workflow> getAll(String name, Pageable pageable) {
        return repository.getAllByName(name, pageable);
    }

    public Optional<Workflow> getById(UUID id) {

        var workflow = repository.findById(id);

        if (workflow.isPresent()) {
            List<WorkflowLevels> workflowLevels = workflowLevelRepository.findByWorkflowId(workflow.get().getId());
            workflow.get().setWorkflowLevels(workflowLevels);
        }
        return workflow;
    }

    public List<Workflow> getByModuleId(UUID id) {
        return getWorkflowLevels(repository.findByModuleId(id));
    }

    public List<Workflow> getByModuleIdAndName(UUID moduleId, String name) {
        return getWorkflowLevels(repository.findByModuleIdAndName(moduleId, name));
    }

    private List<Workflow> getWorkflowLevels(List<Workflow> workflows) {
        return workflows.stream().peek(x -> x.setWorkflowLevels(workflowLevelRepository.findByWorkflowId(x.getId()))).collect(Collectors.toList());
    }
}
