package org.core.backend.ticketapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.entity.TimeZoneEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String eventBanner = "event-banner.jpg";
    private LocalDate eventDate;
    private LocalTime eventTime;
    private boolean recurring = false;
    private TimeZoneEnum timeZone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
