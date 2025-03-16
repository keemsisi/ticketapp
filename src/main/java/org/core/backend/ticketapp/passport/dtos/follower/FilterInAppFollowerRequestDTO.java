package org.core.backend.ticketapp.passport.dtos.follower;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterInAppFollowerRequestDTO {
    private String name;
    private UUID userId;

    @JsonIgnore
    private FollowType type;

    public enum FollowType {
        FOLLOWING, FOLLOWERS
    }
}
