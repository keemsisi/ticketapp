package org.core.backend.ticketapp.event.service;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IService<T> {
    String NOT_IMPLEMENTED = "Not implemented!";

    default <R> T create(R request) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default <R> T update(R request) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default void delete(UUID uuid) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default T getById(UUID id) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }

    default Page<T> getAll(Pageable pageable) {
        throw new NotImplementedException(NOT_IMPLEMENTED);
    }
}
