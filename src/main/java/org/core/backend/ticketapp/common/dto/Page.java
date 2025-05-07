package org.core.backend.ticketapp.common.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private int reqSize;

    public Page(final Long count,
                final List<T> content) {
        this.count = count;
        this.content = content;
    }

    @JsonGetter(value = "last")
    public boolean isLast() {
//        return !this.hasNext();
        return (pageNumber + 1) * reqSize >= totalElements;
    }

    private int getTotalPages() {
        return this.getSize() == 0 ? 1 : (int) Math.ceil((double) this.totalElements / (double) this.getSize());
    }

    private boolean hasNext() {
        return this.pageNumber + 1 < this.getTotalPages();
    }
}
