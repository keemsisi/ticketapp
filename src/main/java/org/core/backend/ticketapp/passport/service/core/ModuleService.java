package org.core.backend.ticketapp.passport.service.core;


import org.core.backend.ticketapp.passport.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ModuleService extends BaseRepoService<Module> {

    @Autowired
    private ModuleRepository repository;

    @Autowired
    public ModuleService(ModuleRepository repository) {
        super(repository);
    }

    public Page<Module> getAll(Pageable pageable) {
        Page<Module> page = repository.findAll(pageable);
        page.forEach(this::resolveFields);
        return page;
    }
}
