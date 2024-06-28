package org.core.backend.ticketapp.passport.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationSocketIOConfiguration {
    @Value(value = "${server.netty-port}")
    private Integer port;
    @Value(value = "${server.netty-socket-origin}")
    private String origin;
    @Autowired
    private RedissonClient redissonClient;

    @Bean
    public SocketIOServer socketIOServer() {
        RedissonStoreFactory redisStoreFactory = new RedissonStoreFactory(redissonClient);
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setStoreFactory(redisStoreFactory);
        config.setPort(port);
        config.setOrigin(origin);
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {
        return new SpringAnnotationScanner(socketIOServer());
    }
}

