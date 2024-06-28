package org.core.backend.ticketapp.passport.service.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.mailchimp.SendMessage;
import org.core.backend.ticketapp.common.mailchimp.To;
import org.core.backend.ticketapp.passport.dtos.core.ReminderNotificationDto;
import org.core.backend.ticketapp.passport.dtos.core.ReminderUpdateDto;
import org.core.backend.ticketapp.passport.dtos.notification.NotificationReadDTO;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.ReminderNotification;
import org.core.backend.ticketapp.passport.repository.ReminderNotificationRepository;
import org.core.backend.ticketapp.passport.service.MailChimpService;
import org.core.backend.ticketapp.passport.service.core.notification.NotificationService;
import org.core.backend.ticketapp.passport.service.core.remindernotification.IReminderNotification;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.SharedEnvironment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ReminderNotificationService implements IReminderNotification {

    @Autowired
    private ReminderNotificationRepository reminderNotificationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private SharedEnvironment sharedEnvironment;
    @Autowired
    private MailChimpService mailChimpService;
    @Autowired
    private NotificationService notificationService;
    @Value("${ticketapp.baseFrontEndUrl}")
    private String baseFrontEndUrl;

    @Override
    public ReminderNotification createNewReminder(ReminderNotificationDto reminderNotificationDto) throws JsonProcessingException {
        ReminderNotification reminderNotification = new ReminderNotification();
        BeanUtils.copyProperties(reminderNotificationDto, reminderNotification);
        reminderNotification.setId(UUID.randomUUID());
        reminderNotification.setDateCreated(LocalDateTime.now());
        reminderNotification.setUserId(jwtTokenUtil.getUser().getUserId());
        reminderNotification.setCreatedBy(jwtTokenUtil.getUser().getUserId());
        reminderNotification.setTenantId(jwtTokenUtil.getUser().getTenantId());
        reminderNotification.setActive(true);
        reminderNotification.setCount(0);
        reminderNotification = reminderNotificationRepository.save(reminderNotification);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), ReminderNotification.class.getTypeName(), null, objectMapper.writeValueAsString(reminderNotification), "Initiated a request to create a reminder notification");
        return reminderNotification;
    }

    @Override
    public void delete(UUID id) throws JsonProcessingException {
        ReminderNotification reminderNotification = getReminderNotificationById(id);
        reminderNotificationRepository.delete(reminderNotification);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), ReminderNotification.class.getTypeName(), objectMapper.writeValueAsString(reminderNotification), String.format("{\"id\": %s,\"isDeleted\": %s}", id, true), "Initiated a request to update reminder notification");
    }

    @Override
    public Page<ReminderNotification> getAll(Date expiryDate, Date dateCreated, Pageable pageable) {
        try {
            UUID userId = jwtTokenUtil.getUser().getUserId();
            if (ObjectUtils.isNotEmpty(expiryDate) && ObjectUtils.isNotEmpty(dateCreated)) {
                return reminderNotificationRepository.getAll(userId, expiryDate, dateCreated, pageable);
            } else if (ObjectUtils.isNotEmpty(expiryDate)) {
                return reminderNotificationRepository.getAllByExpiryDate(userId, expiryDate, pageable);
            } else if (ObjectUtils.isNotEmpty(dateCreated)) {
                return reminderNotificationRepository.getAllByDateCreated(userId, dateCreated, pageable);
            } else return reminderNotificationRepository.getAll(userId, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // TODO: Add snoozing to reminder ( like remind the user in the next x minutes)
    @Override
    public void readNotification(NotificationReadDTO notificationReadDTO) throws JsonProcessingException {
        List<String> oldDataJSON = new ArrayList<>();
        for (UUID notificationId : notificationReadDTO.getNotificationIds()) {
            ReminderNotification reminderNotification = getReminderNotificationById(notificationId);
            if (!reminderNotification.isActive())
                throw new ApplicationException(403, "Forbidden", "Oops! you can't read this reminder... not active");
            if (!LocalDateTime.now().isAfter(reminderNotification.getReminderDate()))
                throw new ApplicationException(409, "Forbidden", "Oops! reminder not due to be read.");
            if (reminderNotification.isRepeat()) {
                if (ObjectUtils.isNotEmpty(notificationReadDTO.getNextReminderDate())) {
                    reminderNotification.setReminderDate(notificationReadDTO.getNextReminderDate());
                } else {
                    reminderNotification.setReminderDate(reminderNotification.getReminderDate().plusDays(1));
                }
                reminderNotification.setRepeat(true);
                reminderNotification.setActive(true);
                reminderNotification.setDeleted(false);
            } else {
                reminderNotification.setRepeat(false);
                reminderNotification.setActive(false);
            }
            reminderNotification.setDateModified(LocalDateTime.now());
            reminderNotification.setModifiedBy(jwtTokenUtil.getUser().getUserId());
            reminderNotification.setCount(reminderNotification.getCount() + 1);
            reminderNotificationRepository.save(reminderNotification);
            oldDataJSON.add(objectMapper.writeValueAsString(reminderNotification));
        }
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), ReminderNotification.class.getTypeName(), objectMapper.writeValueAsString(oldDataJSON), objectMapper.writeValueAsString(notificationReadDTO), "Initiated a request to update reminder notification");
    }

    @Override
    public ReminderNotification getReminderNotificationById(UUID id) {
        Optional<ReminderNotification> reminderNotification = reminderNotificationRepository.findById(id);
        if (reminderNotification.isPresent()) {
            return reminderNotification.get();
        } else
            throw new ApplicationException(404, "Not Found", String.format("Reminder notification with id(%s) not found", id));
    }

    @Override
    public void updateReminderNotification(UUID userId, ReminderUpdateDto reminderUpdateDto) throws JsonProcessingException {
        ReminderNotification reminderNotificationOld;
        String oldDataJSON;
        Optional<ReminderNotification> notificationOptional = reminderNotificationRepository.findById(reminderUpdateDto.getId());
        if (notificationOptional.isPresent()) {
            reminderNotificationOld = notificationOptional.get();
            oldDataJSON = objectMapper.writeValueAsString(reminderNotificationOld);
            BeanUtils.copyProperties(reminderUpdateDto, reminderNotificationOld);
            reminderNotificationOld.setDateModified(LocalDateTime.now());
            reminderNotificationOld.setModifiedBy(userId);
            reminderNotificationRepository.save(reminderNotificationOld);
        } else throw new ApplicationException(404, "Not Found", "Reminder notification not found");
        activityLogProcessorUtils.processActivityLog(userId, ReminderNotification.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(reminderNotificationOld), "Initiated a request to update reminder notification");
    }

    @Override
    public void sendEmailAndInAppReminderNotification() {
        try {
            List<ReminderNotification> allExpiredNotification = reminderNotificationRepository.getAllExpiredReminderNotifications();
            allExpiredNotification.stream().parallel().forEach(reminderNotification -> {
                sendInAppReminderNotification(reminderNotification.getUserId(), reminderNotification);
                reminderNotification.setCount(reminderNotification.getCount() + 1);
                if (reminderNotification.isRepeat()) {
                    reminderNotification.setReminderDate(reminderNotification.getReminderDate().plusDays(1));
                    reminderNotification.setActive(true);
                    reminderNotification.setDeleted(false);
                } else reminderNotification.setActive(false);
                String toEmail = reminderNotification.getUserEmail();
                Context context = new Context();
                context.setVariable("iconUrl", sharedEnvironment.iconUrl);
                context.setVariable("supportEmail", sharedEnvironment.supportEmailAddress);
                context.setVariable("date", new Date().toString());
                context.setVariable("toName", "User");
                context.setVariable("fromName", "TicketApp");
                context.setVariable("description", reminderNotification.getDescription());
                context.setVariable("toMail", toEmail);
                context.setVariable("baseFrontEndUrl", baseFrontEndUrl);
                String html = templateEngine.process("reminder-notification", context);
                var sendMessage =
                        SendMessage.builder()
                                .fromEmail("help@ticketapp.com")
                                .fromName("TicketApp Reminder")
                                .html(html)
                                .subject("TicketApp User Reminder")
                                .to(List.of(To.builder().name("User").email(toEmail).build())).build();
                new Thread(() -> {
                    try {
                        var mailChimpResponse = mailChimpService.send(sendMessage);
                        Arrays.stream(mailChimpResponse).forEach(mailChimpResponse1 -> {
                            log.info(String.format("----||||REMINDER NOTIFICATION MAIL SENDING STATUS||||----> %s", mailChimpResponse1.getStatus()));
                            log.info(String.format("----||||TO MAIL||||----> %s", mailChimpResponse1.getEmail()));
                        });
                    } catch (Exception e) {
                        log.error("----||||ERROR SENDING MAIL----||||", e);
                    }
                }).start();
                reminderNotificationRepository.save(reminderNotification);
            });
        } catch (Exception exception) {
            log.error("{0}", exception);
        }
    }

    private void sendInAppReminderNotification(UUID userId, ReminderNotification reminderNotification) {
        Notification notification = Notification.builder()
                .approvalStatus(null)
                .dateCreated(reminderNotification.getDateCreated())
                .description(reminderNotification.getDescription())
                .requestedBy(reminderNotification.getCreatedBy())
                .id(reminderNotification.getId())
                .tenantId(reminderNotification.getTenantId())
                .notificationType(NotificationType.REMINDER)
                .title(reminderNotification.getTitle())
                .build();
        notificationService.sendNotificationToSingleUser(userId, notification);
    }
}