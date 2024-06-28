package org.core.backend.ticketapp.passport.service.core;


import org.core.backend.ticketapp.passport.entity.Levels;
import org.core.backend.ticketapp.passport.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class LevelService extends BaseRepoService<Levels> {

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository repository) {
        super(repository);
    }

    public Page<Levels> getAll(String name, UUID unitIdS, UUID tenantId, Pageable pageable) {
        UUID departmentIdE = unitIdS; //> 0 ? unitIdS : Long.MAX_VALUE;

        Page<Levels> page = levelRepository.findAll(name, unitIdS, departmentIdE, pageable);

        page.forEach(this::resolveFields);
        return page;
    }

    public Optional<Levels> getByName(String name) {
        return levelRepository.findByName(name);
    }

    public Optional<Levels> getByUUID(UUID id) {
        return levelRepository.findByUUID(id);
    }

    public Optional<Levels> getByUnitIdAndName(UUID departmentId, String name) {
        return levelRepository.findByUnitIdAndName(departmentId, name);
    }
}
