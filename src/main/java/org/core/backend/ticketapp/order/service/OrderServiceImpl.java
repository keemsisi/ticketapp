package org.core.backend.ticketapp.order.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> getAll(final Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order getById(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> new ApplicationException(400, "not_found", "Order not found"));
    }

    @Override
    public void delete(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ApplicationException(400, "not_found", "Order not found"));
        order.setDeleted(true);
        orderRepository.save(order);
    }
}
