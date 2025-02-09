package org.core.backend.ticketapp.marketing.dto.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class FollowUserSocialLinkRequest {
    @NotNull(message = "id can't be null")
    private UUID id;
    @JsonIgnore
    @NotNull(message = "userId can't be null")
    private UUID userId;
}
