package org.core.backend.ticketapp.order.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.PagedMapperUtil;
import org.core.backend.ticketapp.common.dto.PagedResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.transaction.dto.InitPaymentOrderRequestDTO;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController implements ICrudController {
    private final OrderService orderService;
    private final TransactionService transactionService;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<?>> create(@Valid @RequestBody final InitPaymentOrderRequestDTO requestDTO) {
        final var response = transactionService.initializePayment(requestDTO);
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Payment init successful!", response));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<PagedResponse<?>>> getAll(final Pageable pageable) {
        final var orders = PagedMapperUtil.map(orderService.getAll(pageable));
        return new ResponseEntity<>(new GenericApiResponse<>("00", "All orders", orders), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Order>> getById(@PathVariable UUID id) {
        final var data = orderService.getById(id);
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Deleted successfully", null));
    }

}
