package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.FatalObjCopierException;
import io.github.thecarisma.ObjCopier;
import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dtos.notification.CreateWorkflowDTO;
import org.core.backend.ticketapp.passport.dtos.notification.CreateWorkflowLevelDTO;
import org.core.backend.ticketapp.passport.dtos.notification.UpdateWorkflowDTO;
import org.core.backend.ticketapp.passport.entity.ActionWorkflow;
import org.core.backend.ticketapp.passport.entity.Workflow;
import org.core.backend.ticketapp.passport.entity.WorkflowLevels;
import org.core.backend.ticketapp.passport.service.core.WorkflowLevelService;
import org.core.backend.ticketapp.passport.service.core.WorkflowService;
import org.core.backend.ticketapp.passport.util.*;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowLevelService workflowLevelService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody CreateWorkflowDTO createWorkflowDTO) {
        var user = jwtTokenUtil.getUser();

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }
        if (createWorkflowDTO.getLevels().isEmpty()) {
            throw new ApplicationException(400, "400", "A workflow must have at least one level.");
        }
        workflowService.createWorkflow(createWorkflowDTO, user);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Workflow.class.getTypeName(), null, null, "Initiated a request to view a user");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow created successfully.",
                        ""),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name,
                                    @RequestParam(required = false) Integer pageSize,
                                    @RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Sort.Direction order,
                                    @RequestParam(required = false) String[] sortBy
    ) throws ParseException {
        String _name = name != null ? name.toLowerCase() : null;
        Page<Workflow> workflows = workflowService.getAll(_name, ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "created_on"));
        return new ResponseEntity<>(new GenericResponse<>("00", "", workflows),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable(value = "id") UUID workflowId, Pageable pageable) {
        Optional<Workflow> workflow = workflowService.getById(workflowId);
        return new ResponseEntity<>(new GenericResponse<>("00", "", workflow), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/actions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWorkflowActionsById(@PathVariable(value = "id") UUID workflowId) {
        var actions = workflowService.getWorkflowActionById(workflowId);
        return new ResponseEntity<>(new GenericResponse<>("00", "Workflow actions retrieved.", actions),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/actions", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addWorkflowActionsById(@PathVariable(name = "id") UUID workflowId, @RequestBody List<UUID> actionIds) throws JsonProcessingException {
        List<ActionWorkflow> actionWorkflows = null;
        if (actionIds.isEmpty())
            throw new ApplicationException(400, "bad_request", "Kindly provide the list of actions to be added to the workflow.");
        var user = jwtTokenUtil.getUser();
        actionWorkflows = workflowService.createWorkflowActions(actionIds, workflowId, user);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), ActionWorkflow.class.getTypeName(), null, objectMapper.writeValueAsString(actionWorkflows), "Initiated a request to add actions to a workflow");
        return new ResponseEntity<>(new GenericResponse<>("00", "Workflow actions added successfully.", null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/actions", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteWorkflowActionsById(@PathVariable(name = "id") UUID workflowId, @RequestBody List<UUID> actionIds) throws JsonProcessingException {
        List<ActionWorkflow> actionWorkflows = null;
        if (actionIds.isEmpty())
            throw new ApplicationException(400, "bad_request", "Kindly provide the list of actions to be deleted.");
        actionWorkflows = workflowService.deleteActionsByWorkflowId(actionIds, workflowId);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), ActionWorkflow.class.getTypeName(), objectMapper.writeValueAsString(actionWorkflows), null, "Initiated a request to delete set of action workflow");
        return new ResponseEntity<>(new GenericResponse<>("00", "Workflow actions deleted.", null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/modules/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByModuleId(@PathVariable(value = "id") UUID moduleId,
                                           @RequestParam(required = false) String name,
                                           Pageable pageable) {
        List<Workflow> workflows = null;
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(name)) {
            workflows = workflowService.getByModuleIdAndName(moduleId, name);
        } else {
            workflows = workflowService.getByModuleId(moduleId);
        }
        return new ResponseEntity<>(new GenericResponse<>("00", "", workflows), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID workflowId, @Validated @RequestBody UpdateWorkflowDTO createWorkflowDTO) throws FatalObjCopierException, JsonProcessingException {
        String oldDataJSON = null;
        Workflow _updatedWorkflow = null;
        var user = jwtTokenUtil.getUser();

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }

        Optional<Workflow> workflow = workflowService.getByUUID(workflowId);
        if (workflow.isEmpty())
            throw new ApplicationException(400, "400", "No workflow exist with the provided id");
        _updatedWorkflow = workflow.get();
        oldDataJSON = objectMapper.writeValueAsString(_updatedWorkflow);
        ObjCopier.copyFields(_updatedWorkflow, createWorkflowDTO);
        _updatedWorkflow.setActionId(createWorkflowDTO.getActionId());
        _updatedWorkflow.setNormalizedName(StringUtil.normalizeString(createWorkflowDTO.getName()));
        _updatedWorkflow.setModifiedBy(user.getUserId());
        _updatedWorkflow.setModifiedOn(DateTime.now().toDate());

        _updatedWorkflow = workflowService.save(_updatedWorkflow);

        if (!createWorkflowDTO.getLevels().isEmpty()) {
            workflowLevelService.create(createWorkflowDTO.getLevels(), workflowId, user);
        }
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Workflow.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(_updatedWorkflow), "Initiated a request to get a workflow by moduleId");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow updated successfully.", ""),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/levels/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> editWorkFlowLevel(@PathVariable(value = "id") UUID levelId, @Validated @RequestBody CreateWorkflowLevelDTO levelDTO) throws FatalObjCopierException, JsonProcessingException {
        WorkflowLevels updatedWorkflowLevel = null;
        String oldDataJSON = "";
        var user = jwtTokenUtil.getUser();

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }

        Optional<WorkflowLevels> levels = workflowLevelService.getById(levelId);
        if (levels.isEmpty()) throw new ApplicationException(400, "400", "No level exist with the provided id");
        oldDataJSON = objectMapper.writeValueAsString(levels.get());
        var level = new WorkflowLevels();
        BeanUtils.copyProperties(levelDTO, level, ObjectUtils.getNullPropertyNames(levelDTO));
        level.setLevelNo(levelDTO.getLevelNo());
        level.setNormalizedName(StringUtil.normalizeString(levelDTO.getName()));
        level.setModifiedBy(user.getUserId());
        level.setModifiedOn(new Date());

        updatedWorkflowLevel = workflowLevelService.update(levels.get(), level);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), WorkflowLevels.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(updatedWorkflowLevel), "Initiated a request to edit a workflow level");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow level updated successfully.", ""),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/levels/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteWorkFlowLevel(@PathVariable(value = "id") UUID levelId) throws JsonProcessingException {
        WorkflowLevels oldWorkflowLevels = null;
        var user = jwtTokenUtil.getUser();
        Optional<WorkflowLevels> optionalWorkflowLevels = workflowLevelService.getById(levelId);
        if (optionalWorkflowLevels.isEmpty())
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Workflow level does not exists", null),
                    HttpStatus.BAD_REQUEST);
        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }
        oldWorkflowLevels = optionalWorkflowLevels.get();
        workflowLevelService.deleteById(oldWorkflowLevels.getId());
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), WorkflowLevels.class.getTypeName(), objectMapper.writeValueAsString(oldWorkflowLevels), null, "Initiated a request to delete a a unique workflow level");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow level deleted successfully.", ""),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/levels", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewWorkFlowLevel(@Validated @RequestBody CreateWorkflowLevelDTO levelDTO) throws FatalObjCopierException, JsonProcessingException {
        var user = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(user.getRoles(), ConstantUtil.SUPER_ADMIN);
        workflowLevelService.create(List.of(levelDTO), levelDTO.getWorkflowId(), user);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), WorkflowLevels.class.getTypeName(), objectMapper.writeValueAsString(levelDTO), null, "You created a new workflow level");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow level added successfully.", ""),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{workflowId}/levels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWorkflowLevelsByWorkflowId(@PathVariable(value = "workflowId") UUID workflowId) {
        var user = jwtTokenUtil.getUser();
        var levels = workflowLevelService.getByWorkflow(workflowId);
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow level retrieved successfully.", levels),
                HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") UUID workflowId, @PathParam(value = "isForced") boolean isForced) {
        boolean isDeleted = false;
        var user = jwtTokenUtil.getUser();

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }

        List<WorkflowLevels> workflowLevels = workflowLevelService.getByWorkflow(workflowId);

        if (!isForced && workflowLevels.size() > 0) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "This WORKFLOW cannot be deleted because it has LEVEL(s) attached.", ""),
                    HttpStatus.NOT_ACCEPTABLE);
        }

        workflowLevelService.deleteByWorkflowId(workflowId);
        workflowService.deleteAllActionsByWorkflowId(workflowId);

        workflowService.deleteByUUID(workflowId);
        isDeleted = true;
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), WorkflowLevels.class.getTypeName(), null, String.format("{\"workflowId\": %s,\"isDeleted\": %s}", workflowId, isDeleted), "You initiated a request to delete a workflow");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Workflow has been deleted successfully.", ""),
                HttpStatus.OK);
    }
}