package org.core.backend.ticketapp.passport.controller;


import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.passport.service.core.activitylog.IActivityLog;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.ResponsePageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/activities")
public class ActivityLogController {
    @Autowired
    private IActivityLog iActivityLog;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "")
    public ResponseEntity<GenericApiResponse<?>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Sort.Direction order,
            @RequestParam(required = false) boolean paged,
            @RequestParam(required = false) String[] sortBy) {
        Object result = paged ?
                iActivityLog.getUserActivitiesPaged(ResponsePageRequest.createPageRequest(page, size, order, sortBy, paged, "date_created")) :
                iActivityLog.getUserActivities();
        return ResponseEntity.ok().body(new
                GenericApiResponse<>(
                "00",
                "data fetched successfully", result)
        );
    }
}
