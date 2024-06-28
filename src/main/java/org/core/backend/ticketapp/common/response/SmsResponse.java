package org.core.backend.ticketapp.common.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsResponse {
    private String code;
    private String messageId;
    private String message;
    private String balance;
    private String user;
}
