package org.core.backend.ticketapp.plan.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.entity.Plan;
import org.core.backend.ticketapp.plan.service.PlanService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/plans")
public class PlanController implements ICrudController {
    private final PlanService planService;
    private final JwtTokenUtil jwtTokenUtil;
    private final static String FETCHED_SUCCESS_MESSAGE = "Successfully fetched plans";
    private final static String PLAN_SUCCESS_MESSAGE = "Plan created successfully";

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Plan>> create(@Validated @RequestBody PlanCreateRequestDTO request) throws Exception {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var plan = planService.createPlan(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", PLAN_SUCCESS_MESSAGE, plan), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var plans = PagedMapperUtil.map(planService.getAllPlans(pageable));
        return ResponseEntity.ok().body(new GenericApiResponse<>("00", FETCHED_SUCCESS_MESSAGE, plans));
    }
}
