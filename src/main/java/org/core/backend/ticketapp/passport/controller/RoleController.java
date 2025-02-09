package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dao.RoleDao;
import org.core.backend.ticketapp.passport.dao.UserDao;
import org.core.backend.ticketapp.passport.dtos.RoleDto;
import org.core.backend.ticketapp.passport.dtos.core.UserRoleDto;
import org.core.backend.ticketapp.passport.dtos.response.RoleUsersResponse;
import org.core.backend.ticketapp.passport.entity.Role;
import org.core.backend.ticketapp.passport.entity.UserRole;
import org.core.backend.ticketapp.passport.entity.WorkflowLevels;
import org.core.backend.ticketapp.passport.repository.RoleRepository;
import org.core.backend.ticketapp.passport.repository.UserRoleRepository;
import org.core.backend.ticketapp.passport.repository.WorkflowLevelRepository;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ResponsePageRequest;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    @Autowired
    private CoreUserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private WorkflowLevelRepository workflowLevelRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestParam(required = false) String name, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Sort.Direction order, @RequestParam(required = false) String[] sortBy) {
        Page<Role> roles;
        if (Objects.isNull(name)) {
            roles = roleRepository.getAll(ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "created_on"));
        } else {
            roles = roleRepository.getAll(name.toLowerCase(), ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "created_on"));
        }
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", roles), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable(value = "id") UUID roleId) {
        var role = roleRepository.getById(roleId);
        if (Objects.isNull(role)) {
            return new ResponseEntity<>(new GenericApiResponse<>("01", "No role with the specified id found", ""), HttpStatus.NOT_FOUND);
        }
        var roleUsers = roleDao.getRoleUsersByRoleId(roleId);

        var response = RoleUsersResponse.builder().id(role.getId()).code(role.getCode()).name(role.getName()).description(role.getDescription()).users(roleUsers).build();
        return new ResponseEntity<>(new GenericApiResponse<>("00", "", response), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody RoleDto roleDto) throws JsonProcessingException {
        Role newRole;
        var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);

        newRole = userService.createRole(roleDto, loggedInUser);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Role.class.getTypeName(), null, objectMapper.writeValueAsString(newRole), "Initiated a request to get a unique role");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully added the new roles", newRole), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID roleId, @Valid @RequestBody RoleDto role) throws JsonProcessingException {
        String oldDataJSON;
        Role newRole;
        var loggedInUser = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);
        Optional<Role> roleResult = roleRepository.findById(roleId);

        if (!roleResult.isPresent()) {
            return new ResponseEntity<>(new GenericApiResponse<>("01", "The role does not exist", ""), HttpStatus.NOT_FOUND);
        }
        oldDataJSON = objectMapper.writeValueAsString(roleResult.get());
        newRole = userService.updateRole(roleResult.get(), role);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Role.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(newRole), "Initiated a request to update a unique role");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully updated the role", role), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") UUID roleId) throws JsonProcessingException {
        Role role;
        boolean isDeleted;
        Optional<Role> roleResult = userService.getRoleByRoleId(roleId);

        if (!roleResult.isPresent()) {
            return new ResponseEntity<>(new GenericApiResponse<>("01", "No role with the specified id found", ""), HttpStatus.NOT_FOUND);
        }

        Set<UserRole> userRole = userRoleRepository.findAllByRoleId(roleResult.get().getId());

        if (userRole.size() > 0) {
            return new ResponseEntity<>(new GenericApiResponse<>("01", "Role cannot be deleted \n" + "because users are still mapped to the role", ""), HttpStatus.NOT_FOUND);
        }

        List<WorkflowLevels> levels = workflowLevelRepository.findByRoleId(roleResult.get().getId());

        if (levels.size() > 0) {
            return new ResponseEntity<>(new GenericApiResponse<>("01", "Workflow cannot be deleted \n" + "because users are still mapped to the workflow", ""), HttpStatus.NOT_FOUND);
        }

        roleRepository.deleteById(roleId);
        role = roleResult.get();
        isDeleted = true;
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Role.class.getTypeName(), objectMapper.writeValueAsString(role), String.format("{\"roleId\": %s,\"isDeleted\": %s}", roleId, isDeleted), "Initiated a request to delete a role");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully deleted the role", roleResult.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@Validated @RequestBody UserRoleDto userRoleDto) throws JsonProcessingException {
        UserRole userRole;
        boolean isDeleted;
        Optional<UserRole> foundUserRole = userRoleRepository.findByUserIdAndRoleId(userRoleDto.getUserId(), userRoleDto.getRoleId());
        if (foundUserRole.isEmpty())
            return new ResponseEntity<>(new GenericApiResponse<>("01", "User role does not exists.", userRoleDto), HttpStatus.OK);
        userRole = foundUserRole.get();
        userRoleRepository.deleteUserRoleById(userRole.getRoleId(), userRole.getUserId());
        isDeleted = true;
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), UserRole.class.getTypeName(), objectMapper.writeValueAsString(userRole), String.format("{\"roleId\": %s,\"userId\": %s, \"isDeleted\":%s}", userRoleDto.getRoleId(), userRoleDto.getUserId(), isDeleted), "Initiated a request to delete a user role");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully deleted user role", null), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rolesForLoggedInUsers() {
        var userId = UUID.fromString((String) jwtTokenUtil.getClaimByKey("user_id"));
        Optional<Set<UserRole>> roleResult = userDao.getUserRolesById(userId);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully retrieved the user roles", roleResult.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> userRoles(@PathVariable(value = "userId") UUID userId) {
        UserUtils.canAccessResource(userId);
        Optional<Set<UserRole>> roleResult = userDao.getUserRolesById(userId);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully retrieved the user roles", roleResult.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignRolesToUser(@Valid @RequestBody UserRoleDto userRoleDto) throws JsonProcessingException {
        UserRole userRole;
        var loggedInUserDto = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUserDto.getRoles(), ConstantUtil.SUPER_ADMIN);
        userRole = userService.assignRoleToUser(userRoleDto, loggedInUserDto);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), UserRole.class.getTypeName(), objectMapper.writeValueAsString(userRole), null, "Initiated a request to assign role to a user");
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Successfully added the new roles", ""), HttpStatus.OK);
    }
}