package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLHStoreType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "api_client", indexes = {@Index(name = "ix_tbl_api_client_col_client_name_uq", columnList = "normalized_name", unique = true)})
@Getter
@Setter
@TypeDef(typeClass = PostgreSQLHStoreType.class, name = "hstore")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
@TypeDef(name = "hstore", typeClass = PostgreSQLHStoreType.class)
@TypeDef(name = "uuid", typeClass = PostgresUUIDType.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SuppressWarnings("deprecation")
public class Client implements ClientDetails, Serializable {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    @Type(type = "uuid")
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name = "tenant_id")
    private UUID tenantId;

    @NotBlank(message = "{client.name.required}")
    @Column(name = "client_name")
    private String clientName;

    @NotBlank(message = "{client.name.required}")
    @Column(name = "normalized_name")
    private String normalizedName;

    @NotBlank(message = "{client.secret.required}")
    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "client_logo")
    private String clientLogo;

    @Column(name = "client_description")
    private String clientDescription;

    @OrderColumn
    @Type(type = "list-array")
    @Column(name = "scope", columnDefinition = "text[]")
    @JsonIgnore
    private List<String> scopes = Collections.emptyList();

    @JsonIgnore
    @OrderColumn
    @Type(type = "list-array")
    @Column(name = "resources", columnDefinition = "text[]")
    private List<String> resources = Collections.emptyList();

    @JsonIgnore
    @OrderColumn
    @Type(type = "list-array")
    @Column(name="grant_types", columnDefinition = "text[]")
    private List<String> grantTypes = Collections.emptyList();

    @JsonIgnore
    @OrderColumn
    @Type(type = "list-array")
    @Column(name = "redirect_uri", columnDefinition = "text[]")
    private List<String> redirectUri = Collections.emptyList();

    @JsonIgnore
    @OrderColumn
    @Type(type = "list-array")
    @Column(name = "domains", columnDefinition = "text[]")
    private List<String> domains = Collections.emptyList();


    @Column(
            name = "authorities",
            columnDefinition = "text[]"
    )
    @Type(type = "list-array")
    @JsonIgnore
    private List<String> grantedAuthorities = Collections.emptyList();

    @Column(name = "access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;
    @Column(name = "refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;

    @OrderColumn
    @Type(type = "hstore")
    @Column(name = "additional_information", columnDefinition = "hstore")
    private Map<String, Object> additionalInformation = new LinkedHashMap<>();
    @NotBlank(message = "{client.owner.required}")
    @Column(name = "client_owner")
    private String clientOwner;
    @Column(name = "phone_number_required")
    private Boolean phoneNumberRequired = Boolean.FALSE;
    @Column(name = "email_required")
    private Boolean emailRequired = Boolean.FALSE;
    @Column(name = "authentication_email_required")
    private Boolean authenticationEmailRequired = Boolean.FALSE;
    @Column(name = "link_validity_seconds")
    private Integer linkValiditySeconds = 60 * 60 * 24; // default 24 hours

    @Column(name = "created_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date createdOn;

    @Column(name = "modified_on", columnDefinition="TIMESTAMP default CURRENT_TIMESTAMP")
    private Date modifiedOn;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public boolean isAutoApprove(String scope) {
        return true;
    }


    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    public boolean isScoped() {
        return this.scopes != null && !this.scopes.isEmpty();
    }

    public Set<String> getScope() {
        return scopes == null? Collections.emptySet(): new LinkedHashSet<>(scopes);
    }

    public String getClientId(){
        return id.toString();
    }

    public Set<String> getResourceIds() {
        return resources == null? Collections.emptySet() : new LinkedHashSet<>(resources);
    }

    public Set<String> getAuthorizedGrantTypes() {
        return grantTypes == null? Collections.emptySet(): new LinkedHashSet<>(grantTypes);
    }

    public Set<String> getRegisteredRedirectUri() {
        return redirectUri == null? Collections.emptySet(): new LinkedHashSet<>(redirectUri);
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return grantedAuthorities == null? Collections.emptySet(): AuthorityUtils
                .createAuthorityList(grantedAuthorities.toArray(new String[grantedAuthorities.size()]));
    }

    public void setAdditionalInformation(Map<String, ?> additionalInformation) {
        this.additionalInformation = new LinkedHashMap<>(
                additionalInformation);
    }

    public Map<String, Object> getAdditionalInformation() {
        return Collections.unmodifiableMap(this.additionalInformation);
    }
}
