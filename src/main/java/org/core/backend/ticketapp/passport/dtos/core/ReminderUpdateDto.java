package org.core.backend.ticketapp.passport.dtos.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReminderUpdateDto {
    @NotNull
    private UUID id;
    @NotNull
    private LocalDateTime reminderDate;
    private boolean active;
    private boolean repeat;
    private String title;
    private String description;
}
