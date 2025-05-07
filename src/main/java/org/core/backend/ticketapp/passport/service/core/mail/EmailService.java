package org.core.backend.ticketapp.passport.service.core.mail;

import org.apache.commons.lang3.NotImplementedException;
import org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto.MailRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


public interface EmailService {
    String NOT_IMPLEMENTED = "Not implemented!";

    default void sendMail(@Email final String receiver, @NotNull final Object message) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default void sendMail(@Email @NotNull String receiver, @NotNull String templateName) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default void send(@Email @NotNull MailRequest receiver) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }
}
