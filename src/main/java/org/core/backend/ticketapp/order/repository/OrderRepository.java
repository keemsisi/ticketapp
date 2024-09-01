package org.core.backend.ticketapp.order.repository;

import org.core.backend.ticketapp.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query(value = "SELECT * FROM order WHERE batch_id = ?1 AND deleted=false", nativeQuery = true)
    List<Order> findByBatchId(final UUID id);
}
