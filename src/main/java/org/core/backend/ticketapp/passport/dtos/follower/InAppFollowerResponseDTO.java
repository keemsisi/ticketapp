package org.core.backend.ticketapp.passport.dtos.follower;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.passport.entity.InAppFollower;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InAppFollowerResponseDTO extends InAppFollower {
    private String fullName;
    private String profilePicture;
    private LocalDateTime dateFollowed;
}
