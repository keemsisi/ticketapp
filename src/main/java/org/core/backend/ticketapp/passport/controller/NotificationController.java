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
import org.core.backend.ticketapp.passport.service.core.messagebroker.NotificationMessageConsumerService;
import org.core.backend.ticketapp.passport.service.core.notification.NotificationService;
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
    private final NotificationService notificationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final NotificationMessageConsumerService iMessageConsumer;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<?>> getAll(@RequestParam(required = false, defaultValue = "v1") String version, PageRequestParam prp) throws ParseException {
        if (ObjectUtils.isEmpty(prp.getModuleId()))
            UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), "super_admin");
        Object result = prp.isPaged() ? notificationService.getAllByActionNameStatusAndModuleIdPaged(prp.getActionName(), prp.getStatus(), prp.getModuleId(), prp.getStartDate(), prp.getEndDate(), ResponsePageRequest.createPageRequest(prp.getPage(), prp.getSize(), prp.getOrder(), prp.getSortBy(), true, "date_created")) : notificationService.getAllByActionNameStatusAndModuleIdUnPaged(prp.getActionName(), prp.getStatus(), prp.getStartDate(), prp.getEndDate(), prp.getModuleId());
        return ResponseEntity.ok().body(new GenericResponse<>("00", "data fetched successfully", result));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/level-approval")
    public ResponseEntity<GenericResponse<?>> approveRequest(@NotNull @RequestBody SingleRequestApprovalDto singleRequestApprovalDto) throws Exception {
//        System.out.println(bCryptPasswordEncoder.encode("test@123$!!"));
        notificationService.approveRequest(singleRequestApprovalDto);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully", null));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/bulk-level-approval")
    public ResponseEntity<GenericResponse<?>> bulkLevelApproval(@NotNull @RequestBody BulkRequestApprovalDto bulkRequestApprovalDto) throws Exception {
        notificationService.notificationBulkApproval(bulkRequestApprovalDto);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully", bulkRequestApprovalDto.getNotificationId()));
    }


    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, value = "/read-receipt")
    public ResponseEntity<GenericResponse<?>> readNotification(@NotNull @RequestBody NotificationReadDTO notificationReadDTO) {
        notificationService.readNotification(notificationReadDTO);
        return ResponseEntity.ok().body(new GenericResponse<>("00", "read receipt confirmed", notificationReadDTO.getNotificationIds()));
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/unread")
    public ResponseEntity<GenericResponse<?>> getUserNotifications(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
                                                                   @RequestParam(required = false) Sort.Direction order, @RequestParam(required = false) boolean paged,
                                                                   @RequestParam(required = false) String[] sortBy) {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully",
                notificationService.getUserNotificationsByUserIdTenantId(ResponsePageRequest.createPageRequest(page, size, order, sortBy, paged, "date_created"), order)));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/unread/stats")
    public ResponseEntity<GenericResponse<?>> getUserNotifications(@RequestParam() UUID moduleId) throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Request processed successfully", notificationService.getUserUnreadNotificationsStatsByModuleId(moduleId)));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{notificationId}")
    public ResponseEntity<GenericResponse<Notification>> getNotificationById(@NotNull @PathVariable(value = "notificationId") UUID id) {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "notification found", notificationService.getNotificationById(id)));
    }

    //user specific notification approved, declined, rejected and cancelled stats
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/stats/status")
    public ResponseEntity<GenericResponse<UserNotificationApprovalStatusStats>> getUserNotificationStatusStats() throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "status stats fetched successfully", notificationService.getUserNotificationApprovalStatusStats()));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/stats/module")
    public ResponseEntity<GenericResponse<List<UserNotificationModuleStatsWrapper>>> getUserNotificationModuleStats() {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "module stats fetch successfully", notificationService.getUserModuleStats()));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/stats/date-range")
    public ResponseEntity<GenericResponse<List<UserNotificationGroupByDateCreatedWrapper>>> getUserNotificationDateRangeStats(@NotNull @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, @NotNull @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws JsonProcessingException {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "date range stats fetched successfully", notificationService.fetchUserNotificationsReceivedByDateRange(start, end)));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, value = "/create")
    public ResponseEntity<GenericResponse<Object>> createNotification(@NotNull @RequestBody ManualNotificationDTO payload) throws Exception {
        return ResponseEntity.ok().body(new GenericResponse<>("00", "Manual notification created successfully", iMessageConsumer.processMessages(payload.getData().getBytes(), payload.getMessageId())));
    }
}