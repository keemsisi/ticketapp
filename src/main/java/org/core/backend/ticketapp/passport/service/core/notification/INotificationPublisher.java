package org.core.backend.ticketapp.passport.service.core.notification;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.passport.entity.Notification;

import javax.validation.constraints.NotNull;

public interface INotificationPublisher {
    void publishRequest(@NotNull Notification notification) throws JsonProcessingException;
}
