package org.core.backend.ticketapp.marketing.dto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.marketing.common.FollowerStatus;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInAppFollowerRequest extends CreateInAppFollowerRequest {
    @NotNull(message = "id can't be null")
    private UUID id;
    @NotNull(message = "status can't be null")
    private FollowerStatus status;
}
