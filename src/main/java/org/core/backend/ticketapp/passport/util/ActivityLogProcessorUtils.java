package org.core.backend.ticketapp.passport.util;

import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.dtos.HttpServletRequestProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ActivityLogProcessorUtils {
    private final ExecutorService executorService = Executors.newWorkStealingPool(10);
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    private ActivityLogPublisherUtil activityLogPublisherUtil;

    public void processActivityLog(UUID userId, String classTypeName, String oldDataJSON, String newDataJSON, String activityDescription) {
        HttpServletRequestProperty httpServletRequestProperty = activityLogPublisherUtil.getHttpServletRequestProperty(httpServletRequest);
        executorService.submit(() -> activityLogPublisherUtil.publishActivityLog(httpServletRequestProperty, oldDataJSON, newDataJSON, userId, classTypeName,activityDescription));
    }
}