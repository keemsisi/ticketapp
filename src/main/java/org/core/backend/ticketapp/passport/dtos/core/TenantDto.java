package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.core.backend.ticketapp.passport.validation.annotation.CountryCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @Email
    private String email;
    @CountryCode
    private String country;
    @NotBlank
    private String state;
    private String currency;
    private String logoLocation;
    private Integer passwordExpirationInDays;
    private Integer accountLockoutDurationInMinutes;
    private Integer inactivePeriodInMinutes;
    private Integer accountLockoutThresholdCount;
    private boolean emailAlert;
    private boolean smsAlert;
}
