package org.core.backend.ticketapp.passport.service.core;

import io.github.thecarisma.FatalObjCopierException;
import io.github.thecarisma.ObjCopier;
import org.core.backend.ticketapp.passport.entity.Tenant;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public abstract class BaseRepoService<T> {

    public JpaRepository<T, UUID> repository;

    public BaseRepoService() {

    }

    public BaseRepoService(JpaRepository<T, UUID> repository) {
        this.repository = repository;
    }

    public JpaRepository<T, UUID> getRepository() {
        return repository;
    }

    public void resolveFields(T t) {

    }

    public void dissolveFields(T t) {

    }

    public Page<T> getAll(Pageable pageable) {
        Page<T> page = repository.findAll(pageable);
        page.forEach(this::resolveFields);
        return page;
    }

    public T save(T t) {
        t = repository.saveAndFlush(t);
        resolveFields(t);
        return t;
    }

    public Optional<T> getById(UUID id) {
        Optional<T> result = repository.findById(id);
        result.ifPresent(this::resolveFields);
        return result;
    }

    public Optional<T> getById(String id) {
        return Optional.empty();
    }

    public T update(T originalData, T newData) throws FatalObjCopierException {
        ObjCopier.copyFields(originalData, newData);
        dissolveFields(originalData);
        originalData = repository.saveAndFlush(originalData);
        resolveFields(originalData);
        return originalData;
    }

    public T update(T originalData){
        return repository.saveAndFlush(originalData);
    }

    public T updatePut(T originalData, T newData) {
        BeanUtils.copyProperties(newData, originalData);
        dissolveFields(originalData);
        originalData = repository.saveAndFlush(originalData);
        resolveFields(originalData);
        return originalData;
    }

    public void delete(T t) {
        repository.delete(t);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Tenant> getAll(Pageable pageable, String name) {
        return null;
    }
}
