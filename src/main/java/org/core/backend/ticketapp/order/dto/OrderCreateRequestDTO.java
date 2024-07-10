package org.core.backend.ticketapp.order.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCreateRequestDTO() {
}
