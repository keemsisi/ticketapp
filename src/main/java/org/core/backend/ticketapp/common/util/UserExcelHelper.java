package org.core.backend.ticketapp.common.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class UserExcelHelper {
    private HashSet<String> supportedFileFormats = new HashSet<>(Arrays.asList(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/xml"
    ));
    public Boolean isExcelFile (String contentType) {
        return supportedFileFormats.contains(contentType);
    }
}
