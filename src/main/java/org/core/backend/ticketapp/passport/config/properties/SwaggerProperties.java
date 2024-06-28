package org.core.backend.ticketapp.passport.config.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
public class SwaggerProperties {

    @NonNull
    private Boolean enable;
    @NonNull
    private String applicationName;
    @NotNull
    private String applicationDescription;
    @NotNull
    private String applicationVersion;
}
