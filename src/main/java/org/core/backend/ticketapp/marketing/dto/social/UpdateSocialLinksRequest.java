package org.core.backend.ticketapp.marketing.dto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSocialLinksRequest extends CreateSocialLinksRequest {
    @NotNull(message = "id is required!")
    private UUID id;
}
