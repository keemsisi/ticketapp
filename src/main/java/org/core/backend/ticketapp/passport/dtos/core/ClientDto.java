package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Data
public class ClientDto {

    private UUID id = UUID.randomUUID();

    @NotBlank(message = "{client.name.required}")
    private String clientName;
    private String clientLogo;
    private String clientDescription;

    @NotEmpty
    private Collection<String> scope = Collections.emptyList();

    @NotEmpty
    private List<String> resources = Collections.emptyList();

    @NotEmpty
    private List<String> grantTypes = Collections.emptyList();

    private List<String> redirectUri = Collections.emptyList();
    private List<String> domains = Collections.emptyList();

    private List<String> grantedAuthorities = Collections.emptyList();

    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;

    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
    @NotBlank(message = "{client.owner.required}")
    private String clientOwner;
    private Boolean phoneNumberRequired = Boolean.FALSE;
    private Boolean emailRequired = Boolean.FALSE;
    private Boolean authenticationEmailRequired = Boolean.FALSE;
    private Integer linkValiditySeconds = 60 * 60 * 24; // default 24 hours

    public void setAdditionalInformation(Map<String, ?> additionalInformation) {
        this.additionalInformation = new LinkedHashMap<>(
                additionalInformation);
    }

    public void setAdditionalInformation(String key, Object value) {
        this.additionalInformation.put(key, value);
    }
}
