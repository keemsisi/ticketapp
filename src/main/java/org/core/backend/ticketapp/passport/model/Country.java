package org.core.backend.ticketapp.passport.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Country {

    private final String alpha3;
    private final String name;
}
