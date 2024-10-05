package org.core.backend.ticketapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    private List<T> content;
    private long totalElements;
    private long numberOfElements;
    private long pageNumber;
    private long size;
    private boolean last;
    private Long count;

    public Page(final Long count,
                final List<T> content) {
        this.count = count;
        this.content = content;
    }
}
