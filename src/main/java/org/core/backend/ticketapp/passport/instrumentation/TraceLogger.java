/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.core.backend.ticketapp.passport.instrumentation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
public class TraceLogger {

    private final LoggingProperties loggingProperties;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService = Executors.newScheduledThreadPool(10);

    public TraceLogger(LoggingProperties loggingProperties, ObjectMapper objectMapper) {
        this.loggingProperties = loggingProperties;
        this.objectMapper = objectMapper;
    }

    public void log( Trace trace) {
        executorService.submit(() -> {
            try {
                if (Objects.nonNull(trace)) {
                    JsonNode traceNode = objectMapper.readTree(objectMapper.writeValueAsString(trace));
                    log.info("Tracing >>>", StructuredArguments.value("trace", traceNode));
                }
            } catch (Throwable throwable) {
                log.error("Exception logging trace", throwable);
            }
        });
    }
}
