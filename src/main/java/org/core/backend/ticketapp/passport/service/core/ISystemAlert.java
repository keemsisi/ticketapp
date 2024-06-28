package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.dtos.SystemAlertDto;
import org.core.backend.ticketapp.passport.entity.SystemAlert;

import java.util.UUID;

public interface ISystemAlert {
    SystemAlert get(UUID id);

    void update(SystemAlertDto systemAlertDto);
}
