package org.core.backend.ticketapp.common.dto;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static PagedResponse<?> map(Page<? extends Object> paged, final List<? extends Object> contents) {
        return PagedResponse.builder()
                .size(paged.getSize())
                .pageNumber(paged.getNumber())
                .numberOfElements(paged.getNumberOfElements())
                .last(paged.isLast())
                .content(contents)
                .pageNumber(paged.getNumber())
                .totalElements(paged.getTotalElements())
                .paged(paged.getPageable().isPaged())
                .build();
    }
}
