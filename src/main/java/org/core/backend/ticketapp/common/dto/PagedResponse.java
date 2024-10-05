package org.core.backend.ticketapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class PagedResponse<T> {
    private boolean paged;
    private T content;
    private Long totalElements;
    private Integer numberOfElements;
    private Integer pageNumber;
    private Integer size;
    private boolean last;
}
