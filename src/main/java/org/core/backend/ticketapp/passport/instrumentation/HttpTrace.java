package org.core.backend.ticketapp.passport.instrumentation;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HttpTrace extends Trace {

    private String httpUri;
    private String httpMethod;
    private String callerIp;
    private int callerPort;
    private String callerName;
    private String hostIp;
    private int hostPort;
    private String hostName;
    private JsonNode httpRequestHeaders;
    private JsonNode httpRequestParams;
    private JsonNode requestBody;
    private String httpStatusCode;
    private JsonNode responseBody;
}
