package org.core.backend.ticketapp.order.controller;

import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.enums.OrderStatus;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeRequestDTO;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController implements ICrudController {

    OrderService orderService;
    TransactionService transactionService;
    JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Order>> create(@RequestParam UUID eventId, @RequestParam int quantity, @RequestParam BigDecimal amount, @RequestParam String email) {
        // Initialize payment
        String response = transactionService.initializePayment(
                new TransactionInitializeRequestDTO(amount, email != null ? email : jwtTokenUtil.getUser().getEmail(), null, "NGN"));

        Order order = new Order();
        order.setEventId(eventId);
        order.setQuantity(quantity);
        order.setAmount(amount);
        order.setStatus(OrderStatus.PENDING);
        var savedOrder = orderService.save(order);

        return ResponseEntity.ok(new GenericResponse<>("00", response, savedOrder));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<List<Order>>> getAll() throws Exception {
        final var orders = orderService.getAll();
        return new ResponseEntity<>(new GenericResponse<>("00", "All orders", orders), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<Order>> getById(@PathVariable UUID id) {
        final var data = orderService.getById(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Deleted successfully", null));
    }
}
