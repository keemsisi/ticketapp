package org.core.backend.ticketapp.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String type;

    @Column(name = "usage_limit")
    private String usageLimit;


}
