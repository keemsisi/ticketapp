package org.core.backend.ticketapp.common.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class BooleanToIntConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Boolean booleanValue) {
        return (booleanValue ? 1 : 0);
    }

    @Override
    public Boolean convertToEntityAttribute(final Integer dbData) {
        return (dbData == 1);
    }
}
