package org.core.backend.ticketapp.passport.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.dao.UserDao;
import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.repository.NotificationRepository;
import org.core.backend.ticketapp.passport.repository.NotificationSubscriberRepository;
import org.core.backend.ticketapp.passport.repository.UserRepository;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.socketio.dto.Message;
import org.core.backend.ticketapp.passport.socketio.dto.MessageDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

//@Component
@Slf4j
@AllArgsConstructor
public class SocketIOClientConnectionHandler implements ISocketIOClientEventHandler {
    private final SocketIOServer socketIoServer;
    private final UserRepository userRepository;
    private final NotificationSubscriberRepository notificationSubscriberRepository;
    private final NotificationRepository notificationRepository;
    private final CoreUserService coreUserService;
    private final UserDao userDao;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    @OnConnect
    @SuppressWarnings("unchecked")
    public void onConnect(SocketIOClient client) {
        try {
            var token = client.getHandshakeData().getSingleUrlParam("userAuth");
            var userNamePasswordBase64Exploded = new String(Base64.getDecoder().decode(token)).split(":");
            String userName = userNamePasswordBase64Exploded[0], password = userNamePasswordBase64Exploded[1];
            var user = userDao.getUserByEmail(userName).get();
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())
                    || user.isLocked() || user.isDeactivated() || ObjectUtils.isNotEmpty(user.getLockDate()) || user.isDeleted()
                    || ChronoUnit.DAYS.between(user.getPasswordCreatedOn(), Instant.now()) >= user.getPasswordExpirationInDays())
                throw new ApplicationException(401, "unauthorized", "Authentication failure for socket!");
            var permissions = coreUserService.getUserPermissions(user.getId()).get();

            String userId = user.getId().toString();
            UUID userUUID = UUID.fromString(userId);
            List<String> userScope = permissions.getActions();
            permissions.setPassword(null);
            var userData = objectMapper.writeValueAsString(permissions);

            Optional<User> userFound = userRepository.findByUUID(userUUID);
            if (userFound.isPresent()) {
                Optional<NotificationSubscriber> notificationSubscriber = notificationSubscriberRepository.findByUserId(userFound.get().getId());
                if (notificationSubscriber.isPresent()) {
                    updateExistingSubscriber(notificationSubscriber.get(), userData, client, userScope);
                    sendAllUnreadNotificationsToUser(client, userUUID, userScope, userData);
                } else {
                    sendAllUnreadNotificationsToUser(client, userUUID, userScope, userData);
                    storeNewSubscriber(userUUID, userData, client, userScope);
                }
            } else {
                client.sendEvent("new_notification", "Oops! You can't access this service. Make sure you provide valid userAuth");
                client.disconnect();
            }
        } catch (Exception e) {
            client.sendEvent("new_notification", "Oops! You can't access this service. Make sure you provide valid userAuth");
            client.disconnect();
            log.error("CONNECTION ERROR :: ", e);
        }
    }

    private void sendAllUnreadNotificationsToUser(SocketIOClient client, UUID userId, List<String> userScope, String userData) {
        client.sendEvent("new_notification", notificationRepository.getAllUserUnreadNotificationsUnPaged(userId, userScope).stream().parallel().map(notification -> {
            Message message = new Message();
            MessageDto messageDto = new MessageDto(notification.getId(),
                    notification.getRequestedBy(), notification.getRequestedByName(), notification.getActionName(),
                    java.sql.Timestamp.valueOf(notification.getDateCreated()),
                    notification.getModuleId(), new Date(), notification.getApprovalStatus(),
                    NotificationType.SUBSCRIPTION, null,
                    notification.getDescription(), notification.getProcessorStatus(),
                    notification.getProcessorRemark());
            message.setMessage("Unread notification");
            message.setData(messageDto);
            return message;
        }).collect(Collectors.toList()));
        log.info("USER_ID:{} SESSION_ID {} ", userId, client.getSessionId());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        Optional<NotificationSubscriber> optionalNotificationSubscriber = notificationSubscriberRepository.findByCurrentSessionId(client.getSessionId());
        if (optionalNotificationSubscriber.isPresent()) {
            NotificationSubscriber ns = optionalNotificationSubscriber.get();
            ns.setLastSeen(LocalDateTime.now());
            ns.setLastSeenRemoteAddress(client.getRemoteAddress().toString());
            ns.setLastSeenSessionId(client.getSessionId());
            ns.setActive(false);
            ns.setDateModified(LocalDateTime.now());
            notificationSubscriberRepository.save(ns);
            connectDisconnectEventLogger(client, "DISCONNECTED");
        } else {
            log.info("----||||FAILED TO DISCONNECT.... USER SESSION NOT FOUND||||-----");
        }
    }

    public void connectDisconnectEventLogger(SocketIOClient client, String connectionType) {
        log.info("---- |||| REMOTE SOCKET_IO ADDRESS {} {} ||||----", client.getRemoteAddress(), connectionType);
        log.info("---- |||| REMOTE SOCKET_IO SESSION {} {} ||||----", client.getSessionId(), connectionType);
        log.info("---- |||| CLIENT HANDSHAKE_DATA: {} ||||----", client.getHandshakeData().toString());
    }

    @OnEvent(value = "client_message")
    public void onEvent(SocketIOClient client, AckRequest request, Message data) {
        log.info("------ ||||MESSAGE RECEIVED { clientId: {} , messageReceived: {} } |||| ------", client.getSessionId(), data);
        client.sendEvent("messageEventFromServer", "This is a message");
    }

    @OnEvent(value = "unsubscribe")
    public void unSubscribeEvent(SocketIOClient client, AckRequest request, String clientUUID) {
        log.info("------ |||| {} UNSUSCRIBING ------", client.getSessionId());
        if (clientUUID != null) {
            unsubscribeClient(client, clientUUID);
            client.sendEvent("unsubscribed", String.format("Successfully unsubscribed client with sessionId: %s", client.getSessionId()));
        }
    }

    private void unsubscribeClient(SocketIOClient client, String userId) {
        UUID userUUID = UUID.fromString(userId);
        SocketIOClient socketIOClient = socketIoServer.getClient(UUID.fromString(userId));
        Optional<NotificationSubscriber> ns = notificationSubscriberRepository.findByUserId(userUUID);
        if (ns.isPresent()) {
            LocalDateTime localDateTime = LocalDateTime.now();
            NotificationSubscriber notificationSubscriber = ns.get();
            notificationSubscriber.setUnSubscribed(true);
            notificationSubscriber.setLastSeen(localDateTime);
            notificationSubscriber.setLastSeenSessionId(client.getSessionId());
            notificationSubscriber.setLastSeenRemoteAddress(client.getRemoteAddress().toString());
            notificationSubscriber.setDateModified(localDateTime);
            notificationSubscriber.setActive(false);
            notificationSubscriberRepository.save(notificationSubscriber);
            socketIOClient.sendEvent("unsubscribed", "unsubscribe was successful");
            socketIOClient.disconnect();
        } else {
            socketIOClient.sendEvent("unsubscribe_error", "unsubscribe was not successful... user not exists!");
        }
    }

    @Override
    public void storeNewSubscriber(UUID userUUID, String userData, SocketIOClient socketIOClient, List<String> scope) {
        Optional<User> userFound = userRepository.findByUUID(userUUID);
        if (userFound.isPresent()) {
            User user = userFound.get();
            NotificationSubscriber newNotificationSubscriber = new NotificationSubscriber();
            newNotificationSubscriber.setDateSubscribed(LocalDateTime.now());
            newNotificationSubscriber.setActive(true);
            newNotificationSubscriber.setCreatedBy(user.getId());
            newNotificationSubscriber.setId(UUID.randomUUID());
            newNotificationSubscriber.setToken("ignored");
            newNotificationSubscriber.setUserData(userData);
            newNotificationSubscriber.setUserId(userUUID);
            newNotificationSubscriber.setActive(true);
            newNotificationSubscriber.setDeleted(false);
            newNotificationSubscriber.setCurrentSessionId(socketIOClient.getSessionId());
            newNotificationSubscriber.setDateCreated(LocalDateTime.now());
            newNotificationSubscriber.setCurrentSeenRemoteAddress(socketIOClient.getRemoteAddress().toString());
            newNotificationSubscriber.setSubscriberScope(scope);
            newNotificationSubscriber.setUnSubscribed(false);
            notificationSubscriberRepository.save(newNotificationSubscriber);
        } else {
            log.error("----|||| SOCKET_IO CONNECTION NOT PERMITTED FOR USER_ID {}[User not found] ||||----", userUUID);
        }
    }

    @Override
    public void updateExistingSubscriber(NotificationSubscriber notificationSubscriber, String token, SocketIOClient socketIOClient, List<String> scope) {
        notificationSubscriber.setCurrentSeenRemoteAddress(socketIOClient.getRemoteAddress().toString());
        notificationSubscriber.setCurrentSessionId(socketIOClient.getSessionId());
        notificationSubscriber.setToken(token);
        notificationSubscriber.setActive(true);
        notificationSubscriber.setDateModified(LocalDateTime.now());
        notificationSubscriber.setDeleted(false);
        notificationSubscriber.setUnSubscribed(false);
        notificationSubscriber.setLastSeen(LocalDateTime.now());
        notificationSubscriber.setSubscriberScope(scope);
        notificationSubscriberRepository.save(notificationSubscriber);
    }
}