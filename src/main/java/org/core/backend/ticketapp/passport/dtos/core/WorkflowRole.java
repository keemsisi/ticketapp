package org.core.backend.ticketapp.passport.dtos.core;

import io.github.thecarisma.CopyProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
public class WorkflowRole {

    @Id
    @Column(name = "id")
    @CopyProperty(ignore = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    String name;

    @Column(name = "description")
    String description;

    @Transient
    long userCount;
}
