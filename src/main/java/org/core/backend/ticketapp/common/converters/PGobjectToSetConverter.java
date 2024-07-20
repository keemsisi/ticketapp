package org.core.backend.ticketapp.common.converters;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PGobjectToSetConverter implements Converter<PGobject, Set<String>> {

    @Override
    public Set<String> convert(PGobject pgObject) {
        String pgString = pgObject.getValue();
        assert pgString != null;
        return new HashSet<>(Arrays.asList(pgString.split(",")));
    }
}
