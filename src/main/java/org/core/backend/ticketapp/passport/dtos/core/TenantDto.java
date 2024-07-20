package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.passport.validation.annotation.CountryCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
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
    @NotNull(message = "Tenant owner id must be valid!")
    private UUID ownerId;
}
