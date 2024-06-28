package org.core.backend.ticketapp.common.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFADTO {
    private String otp;
    private String purpose;
    private Date dateCreated;
}
