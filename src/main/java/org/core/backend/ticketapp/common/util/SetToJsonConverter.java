package org.core.backend.ticketapp.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@Converter
public class SetToJsonConverter implements AttributeConverter<Set, String> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Set meta) {
        try {
            if (meta == null) {
                return "";
            }
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            //log
            return "";
        }
    }

    @Override
    public Set convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new HashSet();
            }
            return objectMapper.readValue(dbData, Set.class);
        } catch (IOException ex) {
            logger.error("Error occur while decoding json from database: " + dbData, ex);
            return new HashSet();
        }
    }

}
