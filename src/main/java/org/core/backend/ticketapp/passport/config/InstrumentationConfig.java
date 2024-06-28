package org.core.backend.ticketapp.passport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.passport.error.ErrorMapper;
import org.core.backend.ticketapp.passport.instrumentation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@Configuration
public class InstrumentationConfig {

    @Value("${instrumentation.logging-config}")
    private Resource loggingConfig;

    @Bean
    public LoggingProperties loggingProperties(ObjectMapper objectMapper) throws IOException {
        LoggingProperties loggingProperties = objectMapper.readValue(loggingConfig.getInputStream(), LoggingProperties.class);
        RequestUriRouter blacklistedRouter = new RequestUriRouter();
        loggingProperties.getBlacklistedUris().forEach(blacklistedRouter::addRoute);
        loggingProperties.setBlacklistedRouter(blacklistedRouter);
        return loggingProperties;
    }

    @Bean
    public TraceLogger traceLogger(LoggingProperties loggingProperties, ObjectMapper objectMapper) {
        return new TraceLogger(loggingProperties, objectMapper);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean loggingFilter(LoggingProperties loggingProperties, ObjectMapper objectMapper,
                                                TraceLogger traceLogger, ErrorMapper errorMapper) {
        FilterRegistrationBean httpFilterBean =
                new FilterRegistrationBean(new LoggingFilter(loggingProperties, objectMapper, traceLogger, errorMapper));
        httpFilterBean.setUrlPatterns(List.of("/*"));
        httpFilterBean.setOrder(2); // ensure request body is cached before any attempt to read it
        return httpFilterBean;
    }

    @Bean
    public LoggingInterceptor loggingInterceptor(LoggingProperties loggingProperties,
                                                 ObjectMapper objectMapper, TraceLogger traceLogger) {
        return new LoggingInterceptor(loggingProperties, objectMapper, traceLogger);
    }
}
