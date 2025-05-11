package org.core.backend.ticketapp.passport.controller;

import org.core.backend.ticketapp.common.GenericResponse;
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

/**
 * The ApiClientController handles requests related to API clients within the system.
 * It supports creating new clients and retrieving client records by their unique identifier.
 */
@RestController
@RequestMapping("/api/v1/clients")
public class ApiClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    /**
     * Creates a new API client in the system.
     *
     * This method checks if the authenticated user has the SUPER_ADMIN role before allowing the creation of a client.
     * The request includes the client details in the form of a {@link ClientDto}.
     * The operation is logged for activity tracking.
     *
     * @param clientDto the data transfer object containing the details of the client to be created.
     * @return a response entity containing the result of the client creation.
     * @throws Exception if there is an error while processing the client creation.
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody ClientDto clientDto) throws Exception {

        var user = jwtTokenUtil.getUser();

        // Ensure the user has the SUPER_ADMIN role
        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""),
                    HttpStatus.UNAUTHORIZED);
        }

        // Log the activity for creating a client
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Client.class.getTypeName(),
                null, null, "Initiated a request to create an API client");

        // Create the client and return a success response
        return new ResponseEntity<>(new GenericResponse<>("00", "Client created successfully.",
                clientService.create(clientDto, user)), HttpStatus.OK);
    }

    /**
     * Retrieves an API client record by its unique identifier.
     *
     * This method fetches a client based on the provided client ID.
     * If the client is found, it returns the client details; otherwise, an appropriate response is returned.
     *
     * @param clientId the unique identifier of the client to retrieve.
     * @return a response entity containing the client details, or an error message if the client is not found.
     * @throws Exception if there is an error while processing the client retrieval.
     */
    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable UUID clientId) throws Exception {
        // Retrieve the client by its ID and return the result
        return new ResponseEntity<>(new GenericResponse<>("00", "Client record retrieved successfully.",
                clientService.getClientById(clientId)), HttpStatus.OK);
    }
}
