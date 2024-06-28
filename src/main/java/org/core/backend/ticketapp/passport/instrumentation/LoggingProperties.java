package org.core.backend.ticketapp.passport.instrumentation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoggingProperties {

    private List<String> blacklistedUris = new ArrayList<>();
    @JsonIgnore
    private RequestUriRouter blacklistedRouter = new RequestUriRouter();
}
