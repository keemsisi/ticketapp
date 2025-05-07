package org.core.backend.ticketapp.marketing.dto.sponsored_offer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSponsoredOfferRequest extends CreateSponsoredOfferRequest {
    @NotNull
    private UUID id;
}
