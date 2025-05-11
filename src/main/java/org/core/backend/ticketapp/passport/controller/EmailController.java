package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.mailchimp.MailRequest;
import org.core.backend.ticketapp.common.mailchimp.SendMessage;
import org.core.backend.ticketapp.passport.service.MailChimpService;
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

/**
 * The EmailController handles requests for sending emails via the MailChimp service.
 * It allows users to initiate an email send request and logs the activity for tracking purposes.
 */
@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    @Autowired
    private MailChimpService mailChimpService;

    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Sends an email using the MailChimp service.
     *
     * This method accepts a request containing email details (subject, HTML content, recipient) and sends the email
     * using the MailChimp service. The operation is logged for activity tracking.
     *
     * @param request the {@link MailRequest} object containing the email details such as subject, HTML content, and recipient.
     * @return a response entity indicating the status of the email sending operation.
     * @throws JsonProcessingException if there is an error processing the JSON data for logging.
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send(@RequestBody MailRequest request) throws JsonProcessingException {
        // Build the message object from the request data
        var message = SendMessage.builder()
                .subject(request.getSubject())
                .html(request.getHtml())
                .to(request.getTo())
                .build();

        // Send the email using the MailChimp service
        mailChimpService.send(message);

        // Log the activity of sending the email
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), EmailController.class.getTypeName(),
                objectMapper.writeValueAsString(request), null, "Initiated a request to send mail");

        // Return a successful response
        return new ResponseEntity<>(new GenericResponse<>("00", "Email sent successfully", null), HttpStatus.OK);
    }
}
