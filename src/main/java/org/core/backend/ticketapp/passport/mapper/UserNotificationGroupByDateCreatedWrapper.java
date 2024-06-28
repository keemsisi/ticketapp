package org.core.backend.ticketapp.passport.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationGroupByDateCreatedWrapper {
    private String dateCreated;
    private Long count;
}
