package org.core.backend.ticketapp.passport.service.core.messagebroker;

public interface IMessageConsumer {
    Object processMessages(byte[] bytes, String messageId);
}
