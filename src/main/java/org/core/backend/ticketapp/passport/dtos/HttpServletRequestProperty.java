package org.core.backend.ticketapp.passport.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HttpServletRequestProperty {
    private String method;
    private String requestURI;
    private String remoteAddr;
    private String remoteHost;
    private Integer remotePort;
}