package org.core.backend.ticketapp.common.util;

import io.github.thecarisma.ExcelColumnConverter;


public class ExcelColumnToBoolean implements ExcelColumnConverter<Boolean> {
    @Override
    public String convertToCellValue(Boolean b) {
        return b ? "YES" : "NO";
    }

    @Override
    public Boolean convertToFieldValue(String s) {
        return s.toLowerCase().trim().equals("true") || s.toLowerCase().trim().equals("yes");
    }
}

