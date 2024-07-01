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

@RestController
@RequestMapping("/api/v1/reminder-notifications")
public class ReminderNotificationController {
    @Autowired
    private IReminderNotification iReminderNotification;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "")
    public ResponseEntity<GenericResponse<?>> createNewReminderNotification(@Validated @RequestBody ReminderNotificationDto reminderNotificationDto) throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "reminder created", iReminderNotification.createNewReminder(reminderNotificationDto)));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/fetch")
    public ResponseEntity<?> getAll(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expiryDate, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dateCreated, @RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Sort.Direction order, @RequestParam(required = false) String[] sortBy) {
        return new ResponseEntity<>(new GenericResponse<>("00", "Reminder notifications fetched successfully", iReminderNotification.getAll(expiryDate, dateCreated, ResponsePageRequest.createPageRequest(pageNumber, pageSize, order, sortBy, true, "date_created"))), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value = "/read-receipt")
    public ResponseEntity<GenericResponse<?>> readNotification(@Valid @RequestBody NotificationReadDTO notificationReadDTO) throws JsonProcessingException {
        iReminderNotification.readNotification(notificationReadDTO);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "read receipt confirmed", notificationReadDTO.getNotificationIds()));
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, value = "/remove/{id}")
    public ResponseEntity<GenericResponse<?>> deleteReminderNotification(@PathVariable(value = "id") UUID id) throws JsonProcessingException {
        iReminderNotification.delete(id);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "deleted reminder notification with provided id", id));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/view/{id}")
    public ResponseEntity<GenericResponse<ReminderNotification>> getReminderNotificationById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "reminder notification found", iReminderNotification.getReminderNotificationById(id)));
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReminderNotification(@Validated @RequestBody ReminderUpdateDto reminderUpdateDto) throws JsonProcessingException {
        iReminderNotification.updateReminderNotification(jwtTokenUtil.getUser().getUserId(), reminderUpdateDto);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "update was successful", null));
    }
}