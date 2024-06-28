package org.core.backend.ticketapp.passport.util;

import org.core.backend.ticketapp.passport.model.Country;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountryUtil {

    static final Map<String, Country> countries = Stream.of(Locale.getISOCountries())
            .map(code -> {
                Locale locale = new Locale("", code);
                return new Country(locale.getISO3Country(), locale.getDisplayCountry());
            })
            .collect(Collectors.toMap(Country::getAlpha3, country -> country));


    public static List<Country> getCountries() {
        return countries.values().stream()
                .sorted(Comparator.comparing(Country::getName))
                .collect(Collectors.toList());
    }

    public static Country getCountry(String alpha3) {
        return countries.get(alpha3);
    }
}
