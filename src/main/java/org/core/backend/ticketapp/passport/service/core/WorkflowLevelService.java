package org.core.backend.ticketapp.passport.service.core;


import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.notification.CreateWorkflowLevelDTO;
import org.core.backend.ticketapp.passport.entity.WorkflowLevels;
import org.core.backend.ticketapp.passport.repository.WorkflowLevelRepository;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkflowLevelService extends BaseRepoService<WorkflowLevels> {

    @Autowired
    private WorkflowLevelRepository repository;

    @Autowired
    public WorkflowLevelService(WorkflowLevelRepository repository) {
        super(repository);
    }

    public void create(List<CreateWorkflowLevelDTO> createWorkflowLevelDTO, UUID workflowId, LoggedInUserDto user) {
        var workflowLevels = createWorkflowLevelDTO.stream().map(cwl -> {
            WorkflowLevels wl =  new WorkflowLevels();
            wl.setId(UUID.randomUUID());
            wl.setWorkflowId(workflowId);
            wl.setRoleId(cwl.getRoleId());
            wl.setName(cwl.getName());
            wl.setLevelNo(cwl.getLevelNo());
            wl.setNormalizedName(StringUtil.normalizeString(cwl.getName()));
            wl.setDescription(cwl.getDescription());
            wl.setCreatedOn(new Date());
            wl.setCreatedBy(user.getUserId());
            return wl;
        }).collect(Collectors.toList());
        if(!workflowLevels.isEmpty()){
            repository.saveAll(workflowLevels);
        }
    }

    public boolean findModules(UUID workflowId) {
        long moduleExist = repository.countByWorkflowId(workflowId);

        return moduleExist > 0;
    }

    public List<WorkflowLevels> getByWorkflow(UUID workflowId) {

        List<WorkflowLevels> levels = repository.findByWorkflowId(workflowId);

        return levels;
    }


    public int deleteByWorkflowId(UUID workflowId) {
        var wfid = repository.deleteByWorkflowId(workflowId);

        return wfid;
    }

    public void deleteById(UUID levelId) {
        repository.deleteById(levelId);
    }

}
