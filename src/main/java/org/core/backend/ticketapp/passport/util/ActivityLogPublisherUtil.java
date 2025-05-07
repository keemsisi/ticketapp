package org.core.backend.ticketapp.passport.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.dtos.HttpServletRequestProperty;
import org.core.backend.ticketapp.passport.entity.ActivityLog;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.activitylog.IActivityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class ActivityLogPublisherUtil {
    private static final String SERVICE_BUS_TOPIC_NAME = "notification";
    private static final String APPLICATION_MANAGEMENT_SUBSCRIPTION_NAME = "application.management";
    private static final String ACTIVITY_LOG_MESSAGE_ID = "ACTIVITY_LOG";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IActivityLog iActivityLog;
    @Autowired
    private AppConfigs configs;

    public void publishActivityLog(HttpServletRequestProperty httpServletRequest, String oldDataJSON, Object newData, UUID userId, String typeName, String actionDescription) {
        log.info("----||||PROCESSING USER ACTIVITY LOG INSIDE ANOTHER THREAD||||---- {}", Thread.currentThread().getName());
        try {
            final var message = ActivityLog.builder()
                    .activityDescription(actionDescription)
                    .dateCreated(LocalDateTime.now())
                    .oldDataModified(oldDataJSON)
                    .httpMethod(httpServletRequest.getMethod())
                    .httpURI(httpServletRequest.getRequestURI())
                    .id(UUID.randomUUID())
                    .remoteAddress(httpServletRequest.getRemoteAddr())
                    .remoteHost(httpServletRequest.getRemoteHost())
                    .remotePort(httpServletRequest.getRemotePort())
                    .newDataCreated(objectMapper.writeValueAsString(newData))
                    .userId(userId)
                    .entityTypeName(typeName)
                    .build();
            iActivityLog.save(message);
            log.info("----||||USER[{}] ACTIVITY TRACKED||||----", userId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public void saveActivityLog(final UUID userId, String typeName, String oldDataJSON, Object newData, String actionDescription) {
        log.info("----||||PROCESSING USER ACTIVITY LOG INSIDE ANOTHER THREAD||||---- {}", Thread.currentThread().getName());
        try {
            final var message = ActivityLog.builder()
                    .activityDescription(actionDescription)
                    .dateCreated(LocalDateTime.now())
                    .oldDataModified(oldDataJSON)
                    .id(UUID.randomUUID())
                    .remoteAddress("system")
                    .remoteHost("system")
                    .remotePort(configs.port)
                    .newDataCreated(objectMapper.writeValueAsString(newData))
                    .userId(userId)
                    .entityTypeName(typeName)
                    .build();
            iActivityLog.save(message);
            log.info("----||||USER[{}] ACTIVITY TRACKED||||----", userId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void publishActivityLog(HttpServletRequestProperty httpServletRequest, String oldDataJSON, Object newData, UUID userId, String typeName, String actionDescription, UUID tenantId) {
        log.info("----||||PROCESSING USER ACTIVITY LOG INSIDE ANOTHER THREAD||||---- {}", Thread.currentThread().getName());
        try {
            final var message = ActivityLog.builder()
                    .activityDescription(actionDescription)
                    .dateCreated(LocalDateTime.now())
                    .oldDataModified(oldDataJSON)
                    .httpMethod(httpServletRequest.getMethod())
                    .httpURI(httpServletRequest.getRequestURI())
                    .id(UUID.randomUUID())
                    .remoteAddress(httpServletRequest.getRemoteAddr())
                    .remoteHost(httpServletRequest.getRemoteHost())
                    .remotePort(httpServletRequest.getRemotePort())
                    .newDataCreated(objectMapper.writeValueAsString(newData))
                    .userId(userId)
                    .tenantId(tenantId)
                    .entityTypeName(typeName)
                    .build();
            iActivityLog.save(message);
            log.info("----||||USER[{}] ACTIVITY TRACKED||||----", userId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public HttpServletRequestProperty getHttpServletRequestProperty(HttpServletRequest httpServletRequest) {
        return HttpServletRequestProperty.builder().requestURI(httpServletRequest.getRequestURI())
                .method(httpServletRequest.getMethod())
                .remoteAddr(httpServletRequest.getRemoteAddr())
                .remoteHost(httpServletRequest.getRemoteHost())
                .remotePort(httpServletRequest.getRemotePort())
                .build();
    }
}
