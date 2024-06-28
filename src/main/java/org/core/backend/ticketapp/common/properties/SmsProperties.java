package org.core.backend.ticketapp.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "smsApi")
public class SmsProperties {
    @NotBlank
    private String from;
    @NotBlank
    private String api_key;
    @NotBlank
    private String url;
    @NotBlank
    private String type;
    @NotBlank
    private String channel;
}
