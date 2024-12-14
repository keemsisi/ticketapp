package org.core.backend.ticketapp.passport.dtos.follower;

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
}
