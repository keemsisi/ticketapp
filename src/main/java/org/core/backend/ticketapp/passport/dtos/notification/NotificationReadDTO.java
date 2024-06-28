package org.core.backend.ticketapp.passport.dtos.notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReadDTO {
    @NotNull
    private List<UUID> notificationIds;
    private boolean repeat;
    @JsonbDateFormat(value = "yyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime nextReminderDate;
}
