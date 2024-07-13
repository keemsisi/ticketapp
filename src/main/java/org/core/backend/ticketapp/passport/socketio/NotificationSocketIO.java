package org.core.backend.ticketapp.passport.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationSocketIO implements CommandLineRunner {

//    @Autowired
//    private SocketIOServer socketIOServer;

    @Override
    public void run(String... strings) {
//        socketIOServer.start();
//        log.info("---------------|||| [ NOTIFICATION NETTY.SOCKET.IO SERVER STARTED ]|||---------------");
    }
}