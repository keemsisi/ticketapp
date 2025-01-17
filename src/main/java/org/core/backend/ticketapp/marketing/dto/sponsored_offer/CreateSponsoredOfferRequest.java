package org.core.backend.ticketapp.marketing.dto.sponsored_offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSponsoredOfferRequest {
    @NotBlank(message = "title can't be blank")
    private String title;
    @NotNull(message = "validFrom can't be null")
    private LocalDateTime validFrom;
    @NotNull(message = "validFrom can't be null")
    private LocalDateTime validTo;
    @NotNull(message = "details can't be blank")
    private String details;
    private String code;
    @NotNull(message = "discount can't be null")
    private Double discount;
    @NotNull(message = "image can't be blank")
    private String image;
    @NotNull(message = "eventId can't be null")
    private UUID eventId;
}
