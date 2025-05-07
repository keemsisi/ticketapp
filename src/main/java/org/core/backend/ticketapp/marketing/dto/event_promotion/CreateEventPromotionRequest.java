package org.core.backend.ticketapp.marketing.dto.event_promotion;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventPromotionRequest {
    @NotNull(message = "eventId can't be null")
    private UUID eventId;
    @NotBlank(message = "title can't be blank")
    private String title;
    @NotBlank(message = "description can't be blank")
    private String description;
    @NotBlank(message = "image can't be blank")
    private String image;
    @NotBlank(message = "callToAction can't be blank")
    private String callToAction;
    @NotNull(message = "startDate can't be null")
    private LocalDateTime startDate;
    @NotNull(message = "endDate can't be null")
    private LocalDateTime endDate;
}
