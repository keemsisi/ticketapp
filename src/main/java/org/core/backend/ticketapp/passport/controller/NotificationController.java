package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.passport.dtos.PageRequestParam;
import org.core.backend.ticketapp.passport.dtos.notification.*;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.mapper.UserNotificationGroupByDateCreatedWrapper;
import org.core.backend.ticketapp.passport.mapper.UserNotificationModuleStatsWrapper;
import org.core.backend.ticketapp.passport.service.core.messagebroker.IMessageConsumer;
import org.core.backend.ticketapp.passport.service.core.workflow.INotification;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ResponsePageRequest;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@AllArgsConstructor
public class NotificationController {

    private final INotification iNotification;
    private final JwtTokenUtil jwtTokenUtil;
    private final IMessageConsumer iMessageConsumer;

    /**
     * Get all notifications based on parameters such as action name, status, module id, and date range.
     * Optionally, the results can be paginated.
     *
     * @param version the version of the API
     * @param prp the page request parameters
     * @return a response entity with the list of notifications
     * @throws ParseException if there's an issue parsing the date
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<?>> getAll(@RequestParam(required = false, defaultValue = "v1") String version, PageRequestParam prp) throws ParseException {
        if (ObjectUtils.isEmpty(prp.getModuleId()))
            UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), "super_admin");
        Object result = prp.isPaged() ? iNotification.getAllByActionNameStatusAndModuleIdPaged(prp.getActionName(), prp.getStatus(), prp.getModuleId(), prp.getStartDate(), prp.getEndDate(), ResponsePageRequest.createPageRequest(prp.getPage(), prp.getSize(), prp.getOrder(), prp.getSortBy(), true, "date_created")) : iNotification.getAllByActionNameStatusAndModuleIdUnPaged(prp.getActionName(), prp.getStatus(), prp.getStartDate(), prp.getEndDate(), prp.getModuleId());
        return ResponseEntity.ok().body(new GenericResponse<>("00", "data fetched successfully", result));
    }

    /**
     * Approve a single notification request.
     *
     * @param singleRequestApprovalDto the approval details for the notification
     * @return a response entity confirming the request has been processed
     * @throws Exception if there's an error processing the request
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/level-approval")
    public ResponseEntity<GenericResponse<?>> approveRequest(@NotNull @RequestBody SingleRequestApprovalDto singleRequestApprovalDto) throws Exception {
        iNotification.approveRequest(singleRequestApprovalDto);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully", null));
    }

    /**
     * Bulk approve multiple notification requests.
     *
     * @param bulkRequestApprovalDto the bulk approval details for the notifications
     * @return a response entity confirming the bulk approval request has been processed
     * @throws Exception if there's an error processing the request
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/bulk-level-approval")
    public ResponseEntity<GenericResponse<?>> bulkLevelApproval(@NotNull @RequestBody BulkRequestApprovalDto bulkRequestApprovalDto) throws Exception {
        iNotification.notificationBulkApproval(bulkRequestApprovalDto);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully", bulkRequestApprovalDto.getNotificationId()));
    }

    /**
     * Mark notifications as read.
     *
     * @param notificationReadDTO the notification read details
     * @return a response entity confirming the read receipt
     */
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value = "/read-receipt")
    public ResponseEntity<GenericResponse<?>> readNotification(@NotNull @RequestBody NotificationReadDTO notificationReadDTO) {
        iNotification.readNotification(notificationReadDTO);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "read receipt confirmed", notificationReadDTO.getNotificationIds()));
    }

    /**
     * Get unread notifications for the user, with optional pagination.
     *
     * @param page the page number for pagination
     * @param size the page size for pagination
     * @param order the order direction for sorting
     * @param paged whether to paginate the results
     * @param sortBy the sorting criteria
     * @return a response entity with unread notifications
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/unread")
    public ResponseEntity<GenericResponse<?>> getUserNotifications(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                                   @RequestParam(required = false) Sort.Direction order, @RequestParam(required = false) boolean paged,
                                                                   @RequestParam(required = false) String[] sortBy) {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully",
                iNotification.getUserNotificationsByUserIdTenantId(ResponsePageRequest.createPageRequest(page, size, order, sortBy, paged, "date_created"), order)));
    }

    /**
     * Get stats for unread notifications by module ID.
     *
     * @param moduleId the module ID
     * @return a response entity with the unread notification stats for the module
     * @throws JsonProcessingException if there's an error processing the notification stats
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/unread/stats")
    public ResponseEntity<GenericResponse<?>> getUserNotifications(@RequestParam() UUID moduleId) throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully", iNotification.getUserUnreadNotificationsStatsByModuleId(moduleId)));
    }

    /**
     * Get a specific notification by its ID.
     *
     * @param id the notification ID
     * @return a response entity with the notification
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{notificationId}")
    public ResponseEntity<GenericResponse<Notification>> getNotificationById(@NotNull @PathVariable(value = "notificationId") UUID id) {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "notification found", iNotification.getNotificationById(id)));
    }

    /**
     * Get user-specific notification approval status stats (approved, declined, rejected, cancelled).
     *
     * @return a response entity with the stats
     * @throws JsonProcessingException if there's an error processing the stats
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/stats/status")
    public ResponseEntity<GenericResponse<UserNotificationApprovalStatusStats>> getUserNotificationStatusStats() throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "status stats fetched successfully", iNotification.getUserNotificationApprovalStatusStats()));
    }

    /**
     * Get notification module stats for the user.
     *
     * @return a response entity with the module stats
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/stats/module")
    public ResponseEntity<GenericResponse<List<UserNotificationModuleStatsWrapper>>> getUserNotificationModuleStats() {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "module stats fetch successfully", iNotification.getUserModuleStats()));
    }

    /**
     * Get notification stats for a specific date range.
     *
     * @param start the start date for the range
     * @param end the end date for the range
     * @return a response entity with the date range stats
     * @throws JsonProcessingException if there's an error processing the stats
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/stats/date-range")
    public ResponseEntity<GenericResponse<List<UserNotificationGroupByDateCreatedWrapper>>> getUserNotificationDateRangeStats(@NotNull @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @NotNull @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "date range stats fetched successfully", iNotification.fetchUserNotificationsReceivedByDateRange(start, end)));
    }

    /**
     * Create a manual notification.
     *
     * @param payload the manual notification details
     * @return a response entity confirming the notification has been created
     * @throws Exception if there's an error creating the notification
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/create")
    public ResponseEntity<GenericResponse<Object>> createNotification(@NotNull @RequestBody ManualNotificationDTO payload) throws Exception {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Manual notification created successfully", iMessageConsumer.processMessages(payload.getData().getBytes(), payload.getMessageId())));
    }
}
