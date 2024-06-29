package org.core.backend.ticketapp.passport.service.core.messagebroker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record MessageConsumerImpl() implements IMessageConsumer {
    @Override
    public Object processMessages(byte[] bytes, String messageId) {
        return null;
    }
}
