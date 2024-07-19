package org.core.backend.ticketapp.passport.service;


import org.core.backend.ticketapp.passport.entity.Tenant;
import org.core.backend.ticketapp.passport.repository.TenantRepository;
import org.core.backend.ticketapp.passport.service.core.BaseRepoService;
import org.core.backend.ticketapp.passport.service.core.blobstorage.BlobStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;


@Service
public class TenantService extends BaseRepoService<Tenant> {

    @Autowired
    private TenantRepository repository;

    @Autowired
    private BlobStorageService blobStorageService;

    @Autowired
    public TenantService(TenantRepository repository) {
        super(repository);
    }

    public Optional<Tenant> getByTenantId(UUID tenantId) {
        return repository.findRegistrar(tenantId);
    }

    public Tenant updateLogo(Tenant existing, MultipartFile logo) throws IOException, URISyntaxException {
        existing.setLogoLocation(blobStorageService.uploadPicture("registrar_logo", logo));
        return repository.saveAndFlush(existing);
    }
}
