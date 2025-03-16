package org.core.backend.ticketapp.event.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IService<T> {
    String NOT_IMPLEMENTED = "Not implemented!";

    default <R> T create(final R request) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default <R> List<T> createAll(final R request) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default <R> T update(final R request) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default void delete(final UUID uuid) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default T getById(final UUID id) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default Page<T> getAll(final Pageable pageable) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }
}
