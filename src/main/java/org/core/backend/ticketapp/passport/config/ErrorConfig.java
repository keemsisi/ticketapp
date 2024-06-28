package org.core.backend.ticketapp.passport.config;


import org.core.backend.ticketapp.passport.error.ErrorAttributes;
import org.core.backend.ticketapp.passport.error.ErrorMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {

    @Bean
    public ErrorMapper errorMapper() {
        return new ErrorMapper();
    }

    @Bean
    public ErrorAttributes errorAttributes(ErrorMapper errorMapper) {
        return new ErrorAttributes(errorMapper);
    }
}
