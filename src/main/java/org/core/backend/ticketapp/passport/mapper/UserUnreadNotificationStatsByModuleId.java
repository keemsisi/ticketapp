package org.core.backend.ticketapp.passport.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUnreadNotificationStatsByModuleId {
    private String actionName;
    private Long count;
}
