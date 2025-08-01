package org.core.backend.ticketapp.passport.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.passport.dtos.core.TenantDto;
import org.core.backend.ticketapp.passport.entity.SystemAlert;
import org.core.backend.ticketapp.passport.entity.Tenant;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.repository.SystemAlertRepository;
import org.core.backend.ticketapp.passport.repository.TenantRepository;
import org.core.backend.ticketapp.passport.service.core.BaseRepoService;
import org.core.backend.ticketapp.passport.service.core.blobstorage.BlobStorageService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class TenantService extends BaseRepoService<Tenant> {
    private final TenantRepository repository;
    private final SystemAlertRepository systemAlertRepository;
    private final ActivityLogProcessorUtils activityLogProcessorUtils;
    private final BlobStorageService blobStorageService;
    private final ObjectMapper objectMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public Optional<Tenant> getByTenantId(UUID tenantId) {
        return repository.findRegistrar(tenantId);
    }

    public Tenant updateLogo(Tenant existing, MultipartFile logo) throws IOException, URISyntaxException {
        existing.setLogoLocation(blobStorageService.uploadPicture("registrar_logo", logo));
        return repository.saveAndFlush(existing);
    }

    public Tenant create(final TenantDto tenantDto, final User user, final UUID ownerId) throws JsonProcessingException {
        final var tenant = new Tenant();
        BeanUtils.copyProperties(tenantDto, tenant);
        tenant.setId(UUID.randomUUID());
        tenant.setNormalizedName(StringUtil.normalizeString(user.getFullName()));
        tenant.setCreatedBy(user.getId());
        tenant.setCreatedOn(new Date());
        tenant.setUserId(ownerId);
        final var systemAlert = SystemAlert.builder().emailAlert(tenantDto.isEmailAlert())
                .smsAlert(tenantDto.isSmsAlert()).id(UUID.randomUUID())
                .dateCreated(LocalDateTime.now()).createdBy(jwtTokenUtil.getUser().getUserId())
                .tenantId(tenant.getId()).tenantId(tenant.getId()).createdBy(user.getId()).build();
        systemAlertRepository.save(systemAlert);
        tenant.setSystemAlertId(systemAlert.getId());
        final var newTenant = repository.save(tenant);
        activityLogProcessorUtils.processActivityLog(tenant.getId(), user.getId(), Tenant.class.getTypeName(), null,
                objectMapper.writeValueAsString(newTenant), "Created a new tenant account!");
        return newTenant;
    }
}
