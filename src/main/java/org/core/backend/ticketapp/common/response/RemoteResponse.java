package org.core.backend.ticketapp.common.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.core.backend.ticketapp.common.dto.Error;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RemoteResponse {
    @JsonAlias({"code", "ResponseCode", "status", "responseCode"})
    private String responseCode;
    @JsonAlias({"message", "description", "ResponseDescription", "responseDescription"})
    private String responseDescription;
    private List<Error> errors;
    private Error error;
    @JsonAlias({"transactionReference", "transactionRef", "TransactionRef"})
    private String transactionRef;
    private Boolean registerFingerPrintResponse;
    private String transactionIdentifier;
    private String responseMessage;
    private String responseCodeGrouping;
    @JsonProperty(value = "requestError")
    private Map<String, Map<String, String>> messagingServiceRequestError;
}