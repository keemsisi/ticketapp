package org.core.backend.ticketapp.order.repository;

import org.core.backend.ticketapp.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
