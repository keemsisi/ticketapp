package org.core.backend.ticketapp.passport.service.core.remindernotification;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.passport.dtos.core.ReminderNotificationDto;
import org.core.backend.ticketapp.passport.dtos.core.ReminderUpdateDto;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationReadDTO;
import org.core.backend.ticketapp.passport.entity.ReminderNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

public interface IReminderNotification {
    void delete(UUID uuid) throws JsonProcessingException;

    Page<ReminderNotification> getAll(Date expiryDate, Date dateCreated, Pageable pageable);

    void readNotification(NotificationReadDTO notificationReadDTO) throws JsonProcessingException;

    ReminderNotification getReminderNotificationById(UUID id);

    ReminderNotification createNewReminder(ReminderNotificationDto reminderNotificationDto) throws JsonProcessingException;

    void updateReminderNotification(UUID userId, ReminderUpdateDto reminderUpdateDto) throws JsonProcessingException;

    void sendEmailAndInAppReminderNotification();
}
