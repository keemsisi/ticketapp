package org.core.backend.ticketapp.common.converters;

import org.jetbrains.annotations.NotNull;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PGobjectToListConverter implements Converter<PGobject, List<String>> {

    @Override
    public List<String> convert(@NotNull PGobject pgObject) {
        try {
            String[] elements = Objects.requireNonNull(pgObject.getValue()).replaceAll("[{}]", "").split(",");
            return Arrays.asList(elements);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
