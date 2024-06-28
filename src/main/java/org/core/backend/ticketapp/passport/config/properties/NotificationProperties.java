package org.core.backend.ticketapp.passport.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Validated
public class NotificationProperties {

    @NotBlank
    private String sender;
    @NotEmpty
    private List<String> recipients;
    @NotBlank
    private String subject;
    @NotBlank
    private String resourcesUri;
    @NotBlank
    private String appId;
    private Integer batchSize = 25;
}
