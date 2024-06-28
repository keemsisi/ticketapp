package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.thecarisma.CopyProperty;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
public class Workflow {

    @Id
    @Column(name = "id")
    @CopyProperty(ignore = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    UUID id;

    @Column(name = "name")
    @NotBlank(message = "The name cannot be empty")
    String name;

    @Convert(disableConversion = true)
    @CopyProperty(ignore = true)
    @Column(name = "active")
    @Type(type="numeric_boolean")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    boolean active;
}
