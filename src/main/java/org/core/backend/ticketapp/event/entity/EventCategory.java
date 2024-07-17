package org.core.backend.ticketapp.event.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_category")
@Getter
@Setter
public class EventCategory extends AbstractBaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
}
