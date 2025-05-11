package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.passport.dtos.core.ReminderNotificationDto;
import org.core.backend.ticketapp.passport.dtos.core.ReminderUpdateDto;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationReadDTO;
import org.core.backend.ticketapp.passport.entity.ReminderNotification;
import org.core.backend.ticketapp.passport.service.core.remindernotification.IReminderNotification;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ResponsePageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.UUID;

/**
 * Controller for managing reminder notifications in the system.
 */
@RestController
@RequestMapping("/api/v1/reminder-notifications")
public class ReminderNotificationController {

    @Autowired
    private IReminderNotification iReminderNotification;

    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Creates a new reminder notification.
     *
     * @param reminderNotificationDto the DTO containing the reminder notification details
     * @return ResponseEntity containing the generic response with the created reminder notification
     * @throws JsonProcessingException if there is an error processing the JSON
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "")
    public ResponseEntity<GenericResponse<?>> createNewReminderNotification(@Validated @RequestBody ReminderNotificationDto reminderNotificationDto) throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "reminder created", iReminderNotification.createNewReminder(reminderNotificationDto)));
    }

    /**
     * Fetches all reminder notifications with optional filters.
     *
     * @param expiryDate the expiry date filter (optional)
     * @param dateCreated the date created filter (optional)
     * @param pageNumber the page number for pagination (optional)
     * @param pageSize the page size for pagination (optional)
     * @param order the sorting order (optional)
     * @param sortBy the sorting fields (optional)
     * @return ResponseEntity containing the generic response with the list of reminder notifications
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/fetch")
    public ResponseEntity<?> getAll(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expiryDate,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dateCreated,
                                    @RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Integer pageSize,
                                    @RequestParam(required = false) Sort.Direction order,
                                    @RequestParam(required = false) String[] sortBy) {
        return new ResponseEntity<>(new GenericResponse<>("00", "Reminder notifications fetched successfully", iReminderNotification.getAll(expiryDate, dateCreated, ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "date_created"))), HttpStatus.OK);
    }

    /**
     * Marks reminder notifications as read based on the provided notification IDs.
     *
     * @param notificationReadDTO the DTO containing the notification IDs to mark as read
     * @return ResponseEntity containing the generic response with the read notification IDs
     * @throws JsonProcessingException if there is an error processing the JSON
     */
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value = "/read-receipt")
    public ResponseEntity<GenericResponse<?>> readNotification(@Valid @RequestBody NotificationReadDTO notificationReadDTO) throws JsonProcessingException {
        iReminderNotification.readNotification(notificationReadDTO);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "read receipt confirmed", notificationReadDTO.getNotificationIds()));
    }

    /**
     * Deletes a reminder notification by its ID.
     *
     * @param id the ID of the reminder notification to be deleted
     * @return ResponseEntity containing the generic response with the deleted reminder notification ID
     * @throws JsonProcessingException if there is an error processing the JSON
     */
    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/remove/{id}")
    public ResponseEntity<GenericResponse<?>> deleteReminderNotification(@PathVariable(value = "id") UUID id) throws JsonProcessingException {
        iReminderNotification.delete(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "deleted reminder notification with provided id", id));
    }

    /**
     * Fetches a specific reminder notification by its ID.
     *
     * @param id the ID of the reminder notification
     * @return ResponseEntity containing the generic response with the reminder notification
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/view/{id}")
    public ResponseEntity<GenericResponse<ReminderNotification>> getReminderNotificationById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "reminder notification found", iReminderNotification.getReminderNotificationById(id)));
    }

    /**
     * Updates an existing reminder notification.
     *
     * @param reminderUpdateDto the DTO containing the updated reminder notification details
     * @return ResponseEntity containing the generic response confirming the update
     * @throws JsonProcessingException if there is an error processing the JSON
     */
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReminderNotification(@Validated @RequestBody ReminderUpdateDto reminderUpdateDto) throws JsonProcessingException {
        iReminderNotification.updateReminderNotification(jwtTokenUtil.getUser().getUserId(), reminderUpdateDto);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "update was successful", null));
    }
}
