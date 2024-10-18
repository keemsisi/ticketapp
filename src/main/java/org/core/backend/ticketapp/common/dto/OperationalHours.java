package org.core.backend.ticketapp.common.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperationalHours {
    @NotNull(message = "monday operation hour is required")
    private OperationalHour monday;
    @NotNull(message = "tuesday operation hour is required")
    private OperationalHour tuesday;
    @NotNull(message = "wednesday operation hour is required")
    private OperationalHour wednesday;
    @NotNull(message = "thursday operation hour is required")
    private OperationalHour thursday;
    @NotNull(message = "friday operation hour is required")
    private OperationalHour friday;
    @NotNull(message = "saturday operation hour is required")
    private OperationalHour saturday;
    @NotNull(message = "sunday operation hour is required")
    private OperationalHour sunday;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class OperationalHour {
        @NotNull(message = "openingTime is required")
        private LocalDateTime openingTime;
        @NotNull(message = "closingTime is required")
        private LocalDateTime closingTime;
        private boolean open;
    }
}
