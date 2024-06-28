package org.core.backend.ticketapp.passport.error;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.Error;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.response.RemoteResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
@Configuration
public class NoOpResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getBody()))) {
            String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
            var objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            var response = objectMapper.readValue(httpBodyResponse, RemoteResponse.class);
            log.error(objectMapper.writeValueAsString(response));
            if (Objects.nonNull(response.getMessagingServiceRequestError()))
                throw new ApplicationException(httpResponse.getRawStatusCode(),
                        response.getMessagingServiceRequestError().get("serviceException").get("messageId"),
                        response.getMessagingServiceRequestError().get("serviceException").get("text"));
            var error = response.getError();
            var errors = response.getErrors();
            if (Objects.isNull(error) && Objects.nonNull(errors) && !errors.isEmpty()) {
                if (Objects.nonNull(errors)) error = errors.get(0);
                else {
                    error = new Error();
                    error.setCode(response.getResponseCode());
                    error.setMessage(response.getResponseMessage());
                    if (Objects.nonNull(response.getResponseDescription())) {
                        error.setMessage(response.getResponseDescription());
                    }
                }
            }
            if (Objects.isNull(error)) {
                error = new Error();
                error.setCode("" + httpResponse.getRawStatusCode());
                error.setMessage("Request failed, try again or contact support.");
            }
            if (Objects.nonNull(response.getResponseCode())) {
                error = new Error();
                error.setCode(response.getResponseCode());
                error.setMessage(response.getResponseMessage());
            }
            throw new ApplicationException(httpResponse.getRawStatusCode(), error.getCode(), error.getMessage());

        }
    }
}