package org.core.backend.ticketapp.passport.instrumentation;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLogHttpTrace {
    private String httpUri;
    private String httpMethod;
    private Integer remotePort;
    private String remoteAddress;
    private Integer serverPort;
    private String requestContentType;
    private String responseContentType;
    private JsonNode httpRequestHeaders;
    private Map<String, String[]> httpRequestParams;
    private String requestBody;
    private Integer httpStatusCode;
    private String requestAuthType;
    private List<String> requestHeaderNames;
    private String requestQueryString;
    private Collection<String> responseHeaders;
}
