package org.core.backend.ticketapp.passport.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.FatalObjCopierException;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dao.GroupDao;
import org.core.backend.ticketapp.passport.dtos.core.CreateGroupActionDto;
import org.core.backend.ticketapp.passport.dtos.core.GroupDto;
import org.core.backend.ticketapp.passport.dtos.core.UserGroupDto;
import org.core.backend.ticketapp.passport.dtos.group.CreateGroupDTO;
import org.core.backend.ticketapp.passport.entity.Group;
import org.core.backend.ticketapp.passport.entity.GroupActions;
import org.core.backend.ticketapp.passport.entity.GroupUsers;
import org.core.backend.ticketapp.passport.repository.GroupUserRepository;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.service.core.GroupActionService;
import org.core.backend.ticketapp.passport.service.core.GroupService;
import org.core.backend.ticketapp.passport.service.core.GroupUserService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;


@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupActionService groupActionService;

    @Autowired
    private GroupUserService groupUserService;

    @Autowired
    private CoreUserService userService;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody CreateGroupDTO createGroupDTO) throws JsonProcessingException {
        Group response = null;
        var loggedInUser = jwtTokenUtil.getUser();

        if (!loggedInUser.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.NOT_FOUND);
        }

        response = groupService.createGroup(createGroupDTO, loggedInUser);

        boolean isCreatedGroupModuleAction = groupActionService.create(createGroupDTO.getActionIds(), response.getId(), loggedInUser);

        if (isCreatedGroupModuleAction) {
            groupUserService.create(createGroupDTO.getUserIds(), response.getId(), loggedInUser);
        } else {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Error creating group actions", ""),
                    HttpStatus.BAD_REQUEST);
        }
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Group.class.getTypeName(), null, objectMapper.writeValueAsString(response), "Initiated a request to create a group");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Group created successfully.",
                        response),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @RequestParam(required = false) String name, Pageable pageable) throws ParseException {
        String _name = name != null ? name.toLowerCase() : null;

        Page<Group> groups = groupService.getAll(_name, pageable);

        return new ResponseEntity<>(new GenericResponse<>("00", "", groups),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @PathVariable(value = "id") UUID groupId, Pageable pageable) throws ParseException {
        var loggedInUser = jwtTokenUtil.getUser();
        if (!loggedInUser.getRoles().contains((ConstantUtil.SUPER_ADMIN))) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.NOT_FOUND);
        }

        Optional<Group> group = groupService.getById(groupId);
        if (!group.isPresent()) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "No group exist with the given ID", ""),
                    HttpStatus.NOT_FOUND);
        }
        var response = new GroupDto();
        BeanUtils.copyProperties(group.get(), response);
        var groupActions = groupDao.getGroupActionsByGroupId(groupId);
        var groupUsers = groupDao.getGroupUsersByRoleId(groupId);
        response.setActions(groupActions);
        response.setUsers(groupUsers);

        return new ResponseEntity<>(new GenericResponse<>("00", "", response),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID groupId, @Valid @RequestBody CreateGroupDTO createGroupDTO) throws FatalObjCopierException, JsonProcessingException {
        Group _group = null;
        String oldDataJSON = null;
        var loggedInUser = jwtTokenUtil.getUser();

        if (!loggedInUser.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("403", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.FORBIDDEN);
        }

        Optional<Group> group = groupService.getByUUID(groupId);

        if (!group.isPresent()) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "No group exist with the given ID", ""),
                    HttpStatus.NOT_FOUND);
        }
        _group = group.get();
        oldDataJSON = objectMapper.writeValueAsString(_group);
        BeanUtils.copyProperties(createGroupDTO, _group);
        _group.setModifiedBy(loggedInUser.getUserId());
        _group.setModifiedOn(new Date());

        groupService.update(group.get());

        boolean isCreatedGroupActions = groupActionService.create(createGroupDTO.getActionIds(), groupId, loggedInUser);

        if (isCreatedGroupActions) {
            groupUserService.create(createGroupDTO.getUserIds(), groupId, loggedInUser);
        } else {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Error creating module actions", ""),
                    HttpStatus.BAD_REQUEST);
        }
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Group.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(_group), "Initiated a request to update a group");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Group updated successfully.", ""),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") UUID groupId) throws JsonProcessingException {
        boolean isDeleted = false;
        String oldDataJSON = null;
        Optional<Group> group = null;
        var loggedInUser = jwtTokenUtil.getUser();

        if (!loggedInUser.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("403", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.FORBIDDEN);
        }

        oldDataJSON = null;
        group = groupService.getById(groupId);
        if (group.isPresent()) {
            oldDataJSON = objectMapper.writeValueAsString(group.get());
            groupService.deleteByUUID(groupId);
            isDeleted = true;
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Group.class.getTypeName(), oldDataJSON, String.format("{\"groupId\": %s,\"isDeleted\": %s}", groupId, isDeleted), "Initiated a request to delete a group");
            return new ResponseEntity<>(
                    new GenericResponse<>("00", "Group has been successfully deleted.", ""),
                    HttpStatus.OK);
        } else {
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Group.class.getTypeName(), oldDataJSON, String.format("{\"groupId\": %s,\"isDeleted\": %s}", groupId, isDeleted), "Initiated a request to delete a group");
            return new ResponseEntity<>(
                    new GenericResponse<>("400", "Group does not exists.", ""),
                    HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignUsersToGroups(@Valid @RequestBody UserGroupDto userGroupDtos) throws
            JsonProcessingException {
        List<GroupUsers> userGroups = null;
        var loggedInUser = jwtTokenUtil.getUser();
        userGroups = new ArrayList<GroupUsers>();
        for (var userId : userGroupDtos.getUserIds()) {
            var groupUser = new GroupUsers();
            groupUser.setGroupId(userGroupDtos.getGroupId());
            groupUser.setUserId(userId);
            groupUser.setId(UUID.randomUUID());
            groupUser.setCreatedBy(loggedInUser.getUserId());
            groupUser.setCreatedOn(new Date());
            userGroups.add(groupUser);
        }
        groupUserRepository.saveAllAndFlush(userGroups);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Group.class.getTypeName(), null, objectMapper.writeValueAsString(userGroups), "Initiated a request to delete a group");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Successfully added user to new groups",
                        null),
                HttpStatus.OK);
    }

    @RequestMapping(value = "users", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUsersFromGroups(@Valid @RequestBody UserGroupDto userGroupDtos) throws
            JsonProcessingException {
        boolean isDeleted;
        var loggedInUser = jwtTokenUtil.getUser();
        if (!loggedInUser.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.NOT_FOUND);
        }

        for (var userId : userGroupDtos.getUserIds()) {
            groupUserRepository.deleteByUserIdGroupId(userGroupDtos.getGroupId(), userId);
        }
        isDeleted = true;
        List<GroupUsers> groupUsers = groupUserRepository.findByGroupId(userGroupDtos.getGroupId());
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), GroupUsers.class.getTypeName(), objectMapper.writeValueAsString(groupUsers), String.format("{\"groupId\": %s,\"isDeleted\": %s, \"deletedGroupUsers\":%s}", userGroupDtos.getGroupId(), isDeleted, groupUsers), "Initiated a request to delete a group");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Successfully removed user(s) from group",
                        null),
                HttpStatus.OK);

    }

    @RequestMapping(value = "actions", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignActionsToGroups(@Valid @RequestBody CreateGroupActionDto groupAction) throws
            JsonProcessingException {
        boolean newActionsAssignedToGroups = false;
        try {
            var loggedInUser = jwtTokenUtil.getUser();
            newActionsAssignedToGroups = groupActionService.create(groupAction.getActionIds(), groupAction.getGroupId(), loggedInUser);
            return new ResponseEntity<>(
                    new GenericResponse<>("00", "Successfully added actions to group",
                            null),
                    HttpStatus.OK);
        } finally {
            String newDataJSON = null;
            if (newActionsAssignedToGroups) newDataJSON = objectMapper.writeValueAsString(groupAction);
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), GroupActions.class.getTypeName(), null, newDataJSON, "Initiated a request to delete a group");
        }
    }

    @RequestMapping(value = "actions", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteActionsFromGroups(@Valid @RequestBody CreateGroupActionDto groupActionDto) throws
            JsonProcessingException {
        boolean isDeleted = false;
        var loggedInUser = jwtTokenUtil.getUser();
        if (!loggedInUser.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.NOT_FOUND);
        }

        for (var actionId : groupActionDto.getActionIds()) {
            groupActionService.deleteByUserIdGroupId(groupActionDto.getGroupId(), actionId);
        }
        isDeleted = true;
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), GroupActions.class.getTypeName(), objectMapper.writeValueAsString(groupActionDto), String.format("{\"groupId\": %s,\"isDeleted\": %s, \"actionIds\":%s}", groupActionDto.getGroupId(), isDeleted, groupActionDto.getActionIds()), "Initiated a request to delete a group");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Successfully removed actions(s) from group",
                        null),
                HttpStatus.OK);
    }
}