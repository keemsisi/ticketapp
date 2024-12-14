package org.core.backend.ticketapp.passport.dtos.follower;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.passport.entity.InAppFollower;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class FilterInAppFollowerResponseDTO extends InAppFollower {
    private String fullName;
    private String profilePicture;
    private LocalDateTime dateFollowed;
}
