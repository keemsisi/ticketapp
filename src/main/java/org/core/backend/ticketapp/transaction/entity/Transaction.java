package org.core.backend.ticketapp.transaction.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @Column(columnDefinition = "uuid not null default uuid_generate_v4()")
    private UUID id;

    @NotNull
    private String reference;

    @NotNull
    private String string;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @NotNull
    private double amount;

    private LocalDateTime dateCreated;

    private LocalDateTime dateModified;

    @PrePersist
    public void onCreate() {
        id = UUID.randomUUID();
    }
}
