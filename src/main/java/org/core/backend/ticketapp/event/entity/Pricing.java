package org.core.backend.ticketapp.event.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "pricing")
public class Pricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plan_id")
    private Integer planId;

}
