package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dao.ActionDao;
import org.core.backend.ticketapp.passport.dao.UserDao;
import org.core.backend.ticketapp.passport.dtos.core.ActionDto;
import org.core.backend.ticketapp.passport.dtos.core.UserActionDto;
import org.core.backend.ticketapp.passport.entity.Action;
import org.core.backend.ticketapp.passport.entity.UserAction;
import org.core.backend.ticketapp.passport.repository.UserActionRepository;
import org.core.backend.ticketapp.passport.service.core.ActionService;
import org.core.backend.ticketapp.passport.util.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The ActionController handles requests related to actions within the system.
 * It supports CRUD operations on actions, including assigning actions to users.
 */
@RestController
@RequestMapping("/api/v1/actions")
@Validated
public class ActionController {

    @Autowired
    private ActionService actionService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ActionDao actionDao;

    @Autowired
    private UserActionRepository userActionRepository;

    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Creates a new action in the system.
     *
     * @param authorization the authorization header containing the JWT token.
     * @param model the action data transfer object (DTO) to be created.
     * @return a response entity with the result of the action creation.
     * @throws JsonProcessingException if there is an error serializing the action to JSON.
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader(name = "Authorization", defaultValue = "Bearer ") String authorization,
                                    @Valid @RequestBody ActionDto model) throws JsonProcessingException {
        var loggedInUser = jwtTokenUtil.getUser();
        var action = new Action();

        UserUtils.assertUserHasRole(loggedInUser.getRoles(), ConstantUtil.SUPER_ADMIN);

        BeanUtils.copyProperties(model, action);
        action.setId(UUID.randomUUID());
        action.setCode(StringUtil.normalizeWithUnderscore(model.getName()));
        action.setCreatedBy(loggedInUser.getUserId());
        action.setCreatedOn(new Date());
        action.setNormalizedName(StringUtil.normalizeString(model.getName()));
        actionService.save(action);

        activityLogProcessorUtils.processActivityLog(loggedInUser.getUserId(), Action.class.getTypeName(), null,
                objectMapper.writeValueAsString(action), String.format("Initiated a request to create a new action(%s)", action.getDescription()));

        return new ResponseEntity<>(new GenericResponse<>("00", "Action created successfully.", action), HttpStatus.OK);
    }

    /**
     * Creates a list of user actions.
     *
     * @param model the list of user action DTOs to be created.
     * @return a response entity indicating the success of the operation.
     * @throws JsonProcessingException if there is an error serializing the list of user actions to JSON.
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUserActions(@Valid @RequestBody List<UserActionDto> model) throws JsonProcessingException {
        var user = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(user.getRoles(), ConstantUtil.SUPER_ADMIN);
        actionService.saveAllUserActions(model, user);

        activityLogProcessorUtils.processActivityLog(user.getUserId(), Action.class.getTypeName(), null,
                objectMapper.writeValueAsString(model), "Initiated a request to assign a list of actions to yourself.");

        return new ResponseEntity<>(new GenericResponse<>("00", "Action created successfully.", ""), HttpStatus.OK);
    }

    /**
     * Retrieves a list of all actions.
     *
     * @param page the page number for pagination (optional).
     * @param size the number of items per page (optional).
     * @param order the sort order (optional).
     * @param paged whether to paginate the results (optional).
     * @param sortBy the fields by which to sort the results (optional).
     * @return a response entity containing the list of actions.
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllActions(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Sort.Direction order,
            @RequestParam(required = false) boolean paged,
            @RequestParam(required = false) String[] sortBy
    ) {
        var user = jwtTokenUtil.getUser();
        activityLogProcessorUtils.processActivityLog(user.getUserId(), Action.class.getTypeName(), null, null, "Initiated a request to get a list of assigned actions");

        return new ResponseEntity<>(new GenericResponse<>("00", "",
                actionService.getAllActions(ResponsePageRequest.createPageRequest(page, size, order, sortBy, paged, "createdOn"))),
                HttpStatus.OK);
    }

    /**
     * Retrieves a specific action by its ID.
     *
     * @param actionId the ID of the action to retrieve.
     * @return a response entity containing the requested action, or an error message if not found.
     */
    @GetMapping("/{actionId}")
    public ResponseEntity<?> getActionByActionId(
            @PathVariable(name = "actionId") UUID actionId
    ) {
        Optional<Action> action = actionService.getById(actionId);
        if (action.isEmpty()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "No Action was found with this Id", ""),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new GenericResponse<>("00", "", actionService.getById(actionId)), HttpStatus.OK);
    }

    /**
     * Unassigns an action from a user.
     *
     * @param userActionDto the user-action assignment data to be deleted.
     * @return a response entity indicating the success of the operation.
     * @throws JsonProcessingException if there is an error processing the request to delete the user action.
     */
    @DeleteMapping("users")
    public ResponseEntity<?> deleteUserAction(
            @RequestBody UserActionDto userActionDto
    ) throws JsonProcessingException {
        Optional<UserAction> userAction = userActionRepository.findByUserIdAndActionId(userActionDto.getUserId(), userActionDto.getActionId());
        if (!userAction.isPresent()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "No Action was found with this Id", ""),
                    HttpStatus.CONFLICT);
        }

        userActionRepository.delete(userAction.get());
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Action.class.getTypeName(), null,
                null, "Initiated a request to unassign an action from a user.");

        return new ResponseEntity<>(new GenericResponse<>("00", "User action has been deleted successfully", ""), HttpStatus.OK);
    }

    /**
     * Deletes an action by its ID.
     *
     * @param actionId the ID of the action to delete.
     * @return a response entity indicating the success of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAction(
            @PathVariable(name = "id") UUID actionId
    ) {
        Optional<Action> action = actionService.getById(actionId);
        if (!action.isPresent()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "No Action was found with this Id", ""),
                    HttpStatus.CONFLICT);
        }

        actionService.delete(actionId);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Action.class.getTypeName(), null,
                null, "You deleted a unique action.");

        return new ResponseEntity<>(new GenericResponse<>("00", "Action has been deleted successfully", ""), HttpStatus.OK);
    }

    /**
     * Retrieves a list of users assigned to a specific action.
     *
     * @param actionId the ID of the action to retrieve users for.
     * @return a response entity containing the list of user actions.
     */
    @RequestMapping(value = "{actionId}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsersByActionId(@PathVariable(value = "actionId") UUID actionId) {
        var userActions = actionDao.getUsersByActionId(actionId);
        return new ResponseEntity<>(new GenericResponse<>("00", "User actions retrieved successfully.", userActions), HttpStatus.OK);
    }
}
