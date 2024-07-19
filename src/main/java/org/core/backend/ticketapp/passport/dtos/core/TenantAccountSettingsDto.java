package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantAccountSettingsDto {
    @NotNull
    private UUID tenantId;
    @NotNull
    private Integer passwordExpirationInDays;
    @NotNull
    private Integer accountLockoutDurationInMinutes;
    @NotNull
    private Integer inactivePeriodInMinutes;
    @NotNull
    private Integer accountLockoutThresholdCount;
    @NotNull
    private Boolean isTwoFaEnabled;
}
