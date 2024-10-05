package org.core.backend.ticketapp.passport.controller;

import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dtos.core.ClientDto;
import org.core.backend.ticketapp.passport.entity.Client;
import org.core.backend.ticketapp.passport.service.ClientService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
public class ApiClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody ClientDto clientDto) throws Exception {

        var user = jwtTokenUtil.getUser();
        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Client.class.getTypeName(), null, null, "Initiated a request to create an API client");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "client created successfully.",
                        clientService.create(clientDto, user)),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable UUID clientId) throws Exception {
        return new ResponseEntity<>(
                new GenericResponse<>("00", "client record retrieved successfully.",
                        clientService.getClientById(clientId)),
                HttpStatus.OK);
    }
}