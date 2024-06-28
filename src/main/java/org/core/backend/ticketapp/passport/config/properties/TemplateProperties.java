package org.core.backend.ticketapp.passport.config.properties;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class TemplateProperties {

    @NonNull
    private String baseDir;
}
