package org.core.backend.ticketapp.common.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OperationalHours {
    private OperationalHour monday;
    private OperationalHour tuesday;
    private OperationalHour wednesday;
    private OperationalHour thursday;
    private OperationalHour friday;
    private OperationalHour saturday;
    private OperationalHour sunday;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class OperationalHour {
        private LocalDateTime openingTime;
        private LocalDateTime closingTime;
        private boolean open;
    }
}
