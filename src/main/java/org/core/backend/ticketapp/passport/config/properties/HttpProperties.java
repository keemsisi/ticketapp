package org.core.backend.ticketapp.passport.config.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Getter
@Setter
@Validated
public class HttpProperties {

    @NonNull
    private Duration readTimeout;
    @NonNull
    private Duration connectTimeout;
}
