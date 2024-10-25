package org.core.backend.ticketapp.marketing.dto.event_promtion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventPromotionRequest extends CreateEventPromotionRequest {
    @NotNull(message = "id must not be null")
    private UUID id;
}
