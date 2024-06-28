package org.core.backend.ticketapp.passport.dtos.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderNotificationDto {
    private String title;
    @NotNull
    private LocalDateTime reminderDate;
    @NotNull
    private String description;
    private boolean active;
    private boolean repeat;
}
