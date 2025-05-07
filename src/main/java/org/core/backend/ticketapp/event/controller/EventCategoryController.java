package org.core.backend.ticketapp.event.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.event.dto.EventCategoryCreateRequestDTO;
import org.core.backend.ticketapp.event.dto.EventCategoryUpdateRequestDTO;
import org.core.backend.ticketapp.event.entity.EventCategory;
import org.core.backend.ticketapp.event.service.EventCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/event-categories")
@AllArgsConstructor
public class EventCategoryController implements ICrudController {
    private final EventCategoryService categoryService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<EventCategory>> create(@Valid @RequestBody EventCategoryCreateRequestDTO request) {
        final var category = categoryService.create(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Created successfully", category), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<GenericApiResponse<EventCategory>> getById(UUID id) {
        final var category = categoryService.getById(id);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Created successfully", category), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<EventCategory>> update(@RequestBody EventCategoryUpdateRequestDTO request) {
        final var category = categoryService.update(request);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Updated successfully", category),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        List<EventCategory> categories = categoryService.getAll();
        return new ResponseEntity<>(new GenericApiResponse<>("00", "All event categories", categories), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<EventCategory>> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return new ResponseEntity<>(new GenericApiResponse<>("00", "Deleted successfully", null),
                HttpStatus.OK);
    }
}
