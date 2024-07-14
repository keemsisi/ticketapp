package org.core.backend.ticketapp.common.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.UUID;

public interface ICrudController {

    default ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    default <T> ResponseEntity<?> update(@RequestBody @Valid T request) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    default ResponseEntity<?> delete(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    default ResponseEntity<?> getAll(final Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
