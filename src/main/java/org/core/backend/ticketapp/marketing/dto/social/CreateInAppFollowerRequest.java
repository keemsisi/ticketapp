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
public class CreateInAppFollowerRequest {
    @NotNull(message = "followedUserId required")
    private UUID followedUserId;
}
