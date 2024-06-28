package org.core.backend.ticketapp.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;


@Converter
public class JsonToStringConverter implements AttributeConverter<ObjectNode, String> {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ObjectNode meta) {
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
    public ObjectNode convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return objectMapper.createObjectNode();
            }
            return objectMapper.readValue(dbData, ObjectNode.class);
        } catch (IOException ex) {
            logger.error("Error occur while decoding json from database: " + dbData, ex);
            return objectMapper.createObjectNode();
        }
    }

}
