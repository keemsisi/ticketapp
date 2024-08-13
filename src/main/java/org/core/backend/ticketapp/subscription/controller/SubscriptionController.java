package org.core.backend.ticketapp.subscription.controller;

import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.subscription.entity.Subscription;
import org.core.backend.ticketapp.subscription.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class SubscriptionController implements ICrudController {

    SubscriptionService orderService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<List<Subscription>>> getAll() throws Exception {
        final var orders = orderService.getAll();
        return new ResponseEntity<>(new GenericResponse<>("00", "All orders", orders), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Subscription>> getById(@PathVariable UUID id) {
        final var data = orderService.getById(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Deleted successfully", null));
    }
}
