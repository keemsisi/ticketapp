package org.core.backend.ticketapp.marketing.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.marketing.service.JobAdvertisementService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/jobs/advertisements")
@AllArgsConstructor
public class JobAdvertisementController {
    private final JobAdvertisementService service;
    private final JwtTokenUtil jwtTokenUtil;


}
