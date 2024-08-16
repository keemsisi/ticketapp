package org.core.backend.ticketapp.order.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.enums.OrderStatus;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.InitTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController implements ICrudController {
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<?>> create(@RequestBody final InitTransactionRequestDTO requestDTO) {
        final var response = transactionService.initializePayment(requestDTO);
        final var initPaymentData = response.getData();
        final Order order = new Order();
        order.setEventId(requestDTO.getEventId());
        order.setQuantity(requestDTO.getQuantity());
        order.setUserId(jwtTokenUtil.getUser().getUserId());
        order.setAmount(requestDTO.getAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentLink(initPaymentData.getAuthorizationUrl());
        order.setCode(initPaymentData.getAccessCode());
        order.setReference(initPaymentData.getReference());
        var savedOrder = orderService.save(order);
        return ResponseEntity.ok(new GenericResponse<>("00", "Payment init successful!", savedOrder));
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

    @RequestMapping(value = "/callback", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> paymentCallback(final @RequestBody Map<String, Object> paymentCallback) {
        log.info(">>> Vendor Payment Callback Received>>>> {} ", paymentCallback);
        //call the verify payment here when order is successful
        return ResponseEntity.ok().body("Callback received successfully");
    }
}
