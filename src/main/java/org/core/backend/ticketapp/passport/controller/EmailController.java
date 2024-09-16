package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.MailRequest;
import org.core.backend.ticketapp.passport.service.core.mail.EmailService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send(@RequestBody MailRequest request) throws JsonProcessingException {
        emailService.send(request);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), EmailController.class.getTypeName(), objectMapper.writeValueAsString(request), null, "Initiated a request to send mail");
        return new ResponseEntity<>(new GenericResponse<>("00", "Email sent successfully", null), HttpStatus.OK);
    }
}