package org.core.backend.ticketapp.passport.config;


import org.core.backend.ticketapp.passport.config.properties.HttpProperties;
import org.core.backend.ticketapp.passport.error.NoOpResponseErrorHandler;
import org.core.backend.ticketapp.passport.instrumentation.LoggingInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(HttpProperties http,
                                     RestTemplateBuilder builder,
                                     LoggingInterceptor interceptor) {
        return builder
                .errorHandler(new NoOpResponseErrorHandler())
                .additionalInterceptors(interceptor)
                .setConnectTimeout(http.getConnectTimeout())
                .setReadTimeout(http.getReadTimeout())
                .build();
    }
}
