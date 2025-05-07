package org.core.backend.ticketapp.order.repository;

import org.core.backend.ticketapp.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query(value = "SELECT * FROM orders WHERE batch_id = ?1 AND deleted=false", nativeQuery = true)
    List<Order> findByBatchId(final UUID id);

    @Query(value = "SELECT SUM(o.amount) FROM orders o " +
            " WHERE o.ticket_id IS NOT NULL AND o.status IS NOT NULL " +
            " AND o.status = 'COMPLETED' and event_id=?1", nativeQuery = true)
    BigDecimal getTotalEventOrderAmount(final UUID eventId);
}
