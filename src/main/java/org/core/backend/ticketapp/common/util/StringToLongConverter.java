package org.core.backend.ticketapp.common.util;

import io.github.thecarisma.ExcelColumnConverter;

public class StringToLongConverter implements ExcelColumnConverter<Long> {
    @Override
    public String convertToCellValue(Long l) {
        return "l";
    }

    @Override
    public Long convertToFieldValue(String s) {
        return 1L;
    }
}
