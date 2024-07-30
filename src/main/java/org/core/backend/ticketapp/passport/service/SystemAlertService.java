package org.core.backend.ticketapp.passport.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.dtos.SystemAlertDto;
import org.core.backend.ticketapp.passport.entity.SystemAlert;
import org.core.backend.ticketapp.passport.repository.SystemAlertRepository;
import org.core.backend.ticketapp.passport.service.core.ISystemAlert;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SystemAlertService implements ISystemAlert {
    private final SystemAlertRepository systemAlertRepository;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    public SystemAlert get(UUID id) {
        Optional<SystemAlert> optionalSystemAlert = systemAlertRepository.findById(id);
        if (optionalSystemAlert.isPresent()) {
            return optionalSystemAlert.get();
        }
        throw new ApplicationException(400, "not_found", "System alert with id(%s) not found");
    }

    @Override
    public SystemAlert getByTenantId() {
        Optional<SystemAlert> optionalSystemAlert = systemAlertRepository.findByTenantId(jwtTokenUtil.getUser().getTenantId());
        return processSystemAlert(optionalSystemAlert);
    }

    @Override
    public SystemAlert getByTenantId(UUID tenantId) {
        Optional<SystemAlert> optionalSystemAlert = systemAlertRepository.findByTenantId(tenantId);
        return processSystemAlert(optionalSystemAlert);
    }

    private SystemAlert processSystemAlert(final Optional<SystemAlert> systemAlert) {
        if (systemAlert.isPresent()) {
            return systemAlert.get();
        }
        throw new ApplicationException(400, "not_found", "System alert with tenantId not found");
    }

    @Override
    public void update(SystemAlertDto systemAlertDto) {
        SystemAlert systemAlert = getByTenantId();
        systemAlert.setEmailAlert(systemAlertDto.isEmailAlert());
        systemAlert.setSmsAlert(systemAlertDto.isSmsAlert());
        systemAlert.setDateModified(LocalDateTime.now());
        systemAlert.setModifiedBy(jwtTokenUtil.getUser().getUserId());
        systemAlertRepository.save(systemAlert);
    }
}
