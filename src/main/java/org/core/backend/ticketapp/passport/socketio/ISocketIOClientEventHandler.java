package org.core.backend.ticketapp.passport.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.core.backend.ticketapp.passport.entity.NotificationSubscriber;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface ISocketIOClientEventHandler {
    void onConnect(SocketIOClient client) throws ParseException, JsonProcessingException;

    void onDisconnect(SocketIOClient client);

    void connectDisconnectEventLogger(SocketIOClient client, String connectionType);

    void storeNewSubscriber(UUID userId, String token, UUID tenantId, SocketIOClient socketIOClient, List<String> scope) throws JsonProcessingException;

    void updateExistingSubscriber(NotificationSubscriber notificationSubscriber, String token, SocketIOClient socketIOClient, List<String> scope);
}