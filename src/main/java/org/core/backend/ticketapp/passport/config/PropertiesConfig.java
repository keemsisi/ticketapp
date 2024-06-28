package org.core.backend.ticketapp.passport.config;


import org.core.backend.ticketapp.passport.config.properties.HttpProperties;
import org.core.backend.ticketapp.passport.config.properties.SwaggerProperties;
import org.core.backend.ticketapp.passport.config.properties.TemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class PropertiesConfig {

    @Bean
    @ConfigurationProperties(prefix = "http")
    public HttpProperties httpProperties() {
        return new HttpProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "swagger")
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "template")
    public TemplateProperties templateProperties(){
        return new TemplateProperties();
    }
}
