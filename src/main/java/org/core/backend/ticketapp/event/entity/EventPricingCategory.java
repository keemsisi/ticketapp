package org.core.backend.ticketapp.event.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "category")
public class EventPricingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String name;

    @NotBlank private double price;
}