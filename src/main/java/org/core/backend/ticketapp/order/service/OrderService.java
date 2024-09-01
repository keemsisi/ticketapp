package org.core.backend.ticketapp.order.service;

import org.core.backend.ticketapp.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order save(Order order);
    List<Order> saveAll(Collection<Order> order);
    Page<Order> getAll(Pageable pageable);
    Order getById(final UUID id);
    void delete(final UUID id);
}
