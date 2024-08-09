package org.core.backend.ticketapp.order.service;

import org.core.backend.ticketapp.order.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAll();
    Order getById(final UUID id);
    void delete(final UUID id);
}
