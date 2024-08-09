package org.core.backend.ticketapp.order.service;

import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    @Override
    public List<Order> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().toList();
    }

    @Override
    public Order getById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", id.toString()));
    }

    @Override
    public void delete(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found", id.toString()));
        orderRepository.delete(order);
    }
}
