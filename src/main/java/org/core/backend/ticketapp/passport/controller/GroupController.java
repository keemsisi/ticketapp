package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.FatalObjCopierException;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dao.GroupDao;
import org.core.backend.ticketapp.passport.dtos.core.GroupDto;
import org.core.backend.ticketapp.passport.dtos.core.UserGroupDto;
import org.core.backend.ticketapp.passport.dtos.group.CreateGroupDTO;
import org.core.backend.ticketapp.passport.entity.Group;
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
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * GroupController handles all the operations related to groups, including creating, updating, deleting,
 * assigning users, and assigning actions to groups.
 */
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

    /**
     * Creates a new group along with associated actions and users.
     *
     * @param createGroupDTO DTO containing the group information, action IDs, and user IDs
     * @return ResponseEntity with the status of the creation process
     * @throws JsonProcessingException if JSON processing fails
     */
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

    /**
     * Retrieves all groups, optionally filtered by name.
     *
     * @param authorization Authorization header
     * @param name          Optional name filter for the groups
     * @param pageable      Pageable object for pagination
     * @return ResponseEntity containing the list of groups
     * @throws ParseException if the date parsing fails
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @RequestParam(required = false) String name, Pageable pageable) throws ParseException {
        String _name = name != null ? name.toLowerCase() : null;

        Page<Group> groups = groupService.getAll(_name, pageable);

        return new ResponseEntity<>(new GenericResponse<>("00", "", groups),
                HttpStatus.OK);
    }

    /**
     * Retrieves a specific group by its ID, including associated actions and users.
     *
     * @param authorization Authorization header
     * @param groupId       The ID of the group
     * @param pageable      Pageable object for pagination
     * @return ResponseEntity containing the group details
     * @throws ParseException if the date parsing fails
     */
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

    /**
     * Updates an existing group with new data, including group actions and user assignments.
     *
     * @param groupId        The ID of the group to update
     * @param createGroupDTO DTO containing the updated group information, action IDs, and user IDs
     * @return ResponseEntity containing the status of the update process
     * @throws FatalObjCopierException if an error occurs during object copying
     * @throws JsonProcessingException if JSON processing fails
     */
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

    /**
     * Deletes a group by its ID.
     *
     * @param groupId The ID of the group to delete
     * @return ResponseEntity containing the status of the deletion process
     * @throws JsonProcessingException if JSON processing fails
     */
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
            activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Group.class.getTypeName(), oldDataJSON, String.format("{\"groupId\": %s,\"isDeleted\": %s}", groupId.toString(), isDeleted), "Initiated a request to delete a group");
        }
        return new ResponseEntity<>(new GenericResponse<>("00", "Group deleted successfully.", ""),
                HttpStatus.OK);
    }

    /**
     * Assigns users to a group.
     *
     * @param groupId      The ID of the group
     * @param userGroupDto DTO containing the user IDs to assign
     * @return ResponseEntity with the status of the user assignment
     */
    @RequestMapping(value = "/{id}/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignUsers(@PathVariable(value = "id") UUID groupId, @RequestBody UserGroupDto userGroupDto) {
        var loggedInUser = jwtTokenUtil.getUser();

        if (!loggedInUser.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.NOT_FOUND);
        }

        boolean isUsersAssigned = groupUserService.create(userGroupDto.getUserIds(), groupId, loggedInUser);
        if (isUsersAssigned) {
            return new ResponseEntity<>(new GenericResponse<>("00", "Users successfully assigned to the group.", ""),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponse<>("01", "Failed to assign users to the group.", ""),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
