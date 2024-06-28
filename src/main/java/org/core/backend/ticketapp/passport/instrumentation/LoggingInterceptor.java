package org.core.backend.ticketapp.passport.instrumentation;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.json.JsonSanitizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.core.backend.ticketapp.passport.service.core.activitylog.IActivityLog;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final LoggingProperties loggingProperties;
    private final ObjectMapper objectMapper;
    private final TraceLogger traceLogger;
    @Autowired
    private IActivityLog iActivityLog;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public LoggingInterceptor(LoggingProperties loggingProperties, ObjectMapper objectMapper, TraceLogger traceLogger) {
        this.loggingProperties = loggingProperties;
        this.objectMapper = objectMapper;
        this.traceLogger = traceLogger;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (loggingProperties.getBlacklistedRouter().pathMatchesRoute(request.getURI().getPath())) {
            return execution.execute(request, body);
        } else {
            long startExecution = System.currentTimeMillis();
            BufferingClientHttpResponseWrapper responseWrapper = null;
            try {
                responseWrapper = new BufferingClientHttpResponseWrapper(execution.execute(request, body));
                return responseWrapper;
            } finally {
                long duration = System.currentTimeMillis() - startExecution;
                HttpTrace trace = buildTrace(request, body, responseWrapper, duration);
                traceLogger.log(trace);
            }
        }
    }

    private HttpTrace buildTrace(HttpRequest request, byte[] body, ClientHttpResponse response, long duration) throws IOException {
        HttpTrace trace = new HttpTrace();
        trace.setHttpUri(request.getURI().toURL().toString());
        trace.setHttpMethod(request.getMethodValue());
        trace.setHostName(request.getURI().getHost());
        trace.setHostPort(request.getURI().getPort());
        trace.setHttpRequestHeaders(getRequestHeaders(request));
        trace.setHttpRequestParams(getRequestParams(request));
        trace.setRequestBody(getRequestBody(request, body));
        if (Objects.nonNull(response)) {
            trace.setHttpStatusCode(String.valueOf(response.getRawStatusCode()));
        }
        trace.setResponseBody(getResponseBody(response));
        trace.setDuration(duration);
        return trace;
    }

    private JsonNode getRequestHeaders(HttpRequest request) throws IOException {
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        return getJson(objectMapper.writeValueAsString(headers));
    }

    private JsonNode getRequestParams(HttpRequest request) throws IOException {
        List<NameValuePair> params =
                URLEncodedUtils.parse(request.getURI().getQuery(), getCharset(request.getHeaders()));
        return getJson(objectMapper.writeValueAsString(params));
    }

    private JsonNode getRequestBody(HttpRequest request, byte[] bodyBytes) throws IOException {
        Charset charset = getCharset(request.getHeaders());
        MediaType contentType = request.getHeaders().getContentType();
        String body = new String(bodyBytes, charset);
        if (Objects.nonNull(contentType) && StringUtils.isNotBlank(body)) {
            if (contentType.toString().contains(APPLICATION_JSON)) {
                return getJson(body);
            } else if (contentType.toString().contains(APPLICATION_FORM_URL_ENCODED)) {
                List<NameValuePair> params = URLEncodedUtils.parse(body, charset);
                return getJson(objectMapper.writeValueAsString(params));
            }
        }
        return null;
    }

    public JsonNode getResponseBody(ClientHttpResponse response) {
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(response)) {
            try (BufferedReader bufferedReader =
                         new BufferedReader(new InputStreamReader(response.getBody(), getCharset(response.getHeaders())))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                String body = builder.toString();
                MediaType contentType = response.getHeaders().getContentType();
                if (StringUtils.isNotBlank(body) && Objects.nonNull(contentType)) {
                    if (contentType.toString().contains(APPLICATION_JSON)) {
                        return getJson(body);
                    } else {
                        return objectMapper.getNodeFactory().textNode(body);
                    }
                }
            } catch (Exception e) {
                log.error("Exception extracting response body", e);
            }
        }
        return null;
    }

    private Charset getCharset(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (Objects.nonNull(contentType) && Objects.nonNull(contentType.getCharset())) {
            return contentType.getCharset();
        }
        return DEFAULT_CHARSET;
    }

    private JsonNode getJson(String data) {
        try {
            return objectMapper.readTree(JsonSanitizer.sanitize(data));
        } catch (IOException e) {
            log.error("Exception building json object", e);
        }
        return null;
    }
}
