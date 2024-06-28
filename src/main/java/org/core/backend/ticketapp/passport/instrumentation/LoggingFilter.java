package org.core.backend.ticketapp.passport.instrumentation;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.json.JsonSanitizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.core.backend.ticketapp.common.response.ErrorResponse;
import org.core.backend.ticketapp.passport.error.ErrorMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final Set<String> REQUEST_BODY_METHODS = Set.of("POST", "PUT", "PATCH");
    private static final Set<String> RESPONSE_CONTENT_TYPES = Set.of(APPLICATION_JSON);

    private final LoggingProperties loggingProperties;
    private final ObjectMapper objectMapper;
    private final TraceLogger traceLogger;
    private final ErrorMapper errorMapper;

    public LoggingFilter(LoggingProperties loggingProperties, ObjectMapper objectMapper,
                         TraceLogger traceLogger, ErrorMapper errorMapper) {
        this.loggingProperties = loggingProperties;
        this.objectMapper = objectMapper;
        this.traceLogger = traceLogger;
        this.errorMapper = errorMapper;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain) throws ServletException, IOException {
        if (loggingProperties.getBlacklistedRouter().pathMatchesRoute(request.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            long startExecution = System.currentTimeMillis();
            try {
                chain.doFilter(requestWrapper, responseWrapper);
            } catch (NestedServletException e) {
                ErrorResponse errorResponse = errorMapper.throwableToErrorResponse(e.getCause());
                responseWrapper.setStatus(errorResponse.getHttpStatus());
                responseWrapper.setContentType(request.getContentType());
                PrintWriter writer = responseWrapper.getWriter();
                objectMapper.writeValue(writer, errorResponse);
            } catch (Exception e) {
                ErrorResponse errorResponse = errorMapper.throwableToErrorResponse(e.getCause());
                responseWrapper.setStatus(errorResponse.getHttpStatus());
                responseWrapper.setContentType(request.getContentType());
                PrintWriter writer = responseWrapper.getWriter();
                objectMapper.writeValue(writer, errorResponse);
            }finally {
                    long duration = System.currentTimeMillis() - startExecution;
                HttpTrace trace = buildTrace(requestWrapper, responseWrapper, duration);
                traceLogger.log(trace);
                responseWrapper.copyBodyToResponse();
            }
        }
    }

    @Override
    public void destroy() {
    }

    private HttpTrace buildTrace(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) throws IOException {
        HttpTrace trace = new HttpTrace();
        trace.setHttpUri(request.getRequestURL().toString());
        trace.setHttpMethod(request.getMethod());
        trace.setCallerIp(request.getRemoteAddr());
        trace.setCallerName(request.getRemoteHost());
        trace.setCallerPort(request.getRemotePort());
        trace.setHostIp(request.getLocalAddr());
        trace.setHostName(request.getLocalName());
        trace.setHostPort(request.getLocalPort());
        trace.setHttpRequestHeaders(getRequestHeaders(request));
        trace.setHttpRequestParams(getRequestParams(request));
        trace.setRequestBody(getRequestBody(request));
        trace.setHttpStatusCode(String.valueOf(response.getStatus()));
        trace.setResponseBody(getResponseBody(response));
        trace.setDuration(duration);
        return trace;
    }

    private JsonNode getRequestHeaders(HttpServletRequest request) throws IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        HashMap<String, String> headerValues = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerValues.put(key, value);
        }
        return getJson(objectMapper.writeValueAsString(headerValues));
    }

    private JsonNode getRequestParams(ContentCachingRequestWrapper request) throws IOException {
        List<NameValuePair> params =
                URLEncodedUtils.parse(request.getQueryString(), Charsets.toCharset(request.getCharacterEncoding()));
        if (Objects.nonNull(params) && !params.isEmpty()) {
            return getJson(objectMapper.writeValueAsString(params));
        }
        return null;
    }

    private JsonNode getRequestBody(ContentCachingRequestWrapper request) throws IOException {
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && REQUEST_BODY_METHODS.contains(request.getMethod())) {
            if (contentType.contains(APPLICATION_JSON)) {
                return getJson(new String(request.getContentAsByteArray(), request.getCharacterEncoding()));
            } else if (contentType.contains(APPLICATION_FORM_URL_ENCODED)) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                if (Objects.nonNull(parameterMap)) {
                    return getJson(objectMapper.writeValueAsString(parameterMap));
                }
            }
        }
        return null;
    }

    private JsonNode getResponseBody(ContentCachingResponseWrapper response) throws IOException {
        if (StringUtils.isNotBlank(response.getContentType())) {
            String contentType = response.getContentType().split(";")[0];
            String responseString = new String(response.getContentAsByteArray(), response.getCharacterEncoding());
            if (RESPONSE_CONTENT_TYPES.contains(contentType)) {
                return getJson(responseString);
            } else {
                return objectMapper.getNodeFactory().textNode(responseString);
            }
        }
        return null;
    }

    private JsonNode getJson(String data) {
        try {
            return objectMapper.readTree(JsonSanitizer.sanitize(data));
        } catch (IOException e) {
            logger.error("Exception building json object", e);
        }
        return null;
    }
}
