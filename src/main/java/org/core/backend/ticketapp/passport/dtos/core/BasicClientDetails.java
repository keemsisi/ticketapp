package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BasicClientDetails {

    private UUID id;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "client_logo")
    private String clientLogo;
    @Column(name = "client_owner")
    private String clientOwner;
    private List<String> domains = Collections.emptyList();
}
