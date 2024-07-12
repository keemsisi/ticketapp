package org.core.backend.ticketapp.common;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
public class PagedMapperUtil {

    public static PagedResponse<?> map(Page<? extends Object> paged) {
        return PagedResponse.builder()
                .size(paged.getSize())
                .pageNumber(paged.getNumber())
                .numberOfElements(paged.getNumberOfElements())
                .last(paged.isLast())
                .content(paged.getContent())
                .pageNumber(paged.getNumber())
                .totalElements(paged.getTotalElements())
                .paged(paged.getPageable().isPaged())
                .build();
    }
}
