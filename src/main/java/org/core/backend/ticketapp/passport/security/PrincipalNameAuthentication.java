package org.core.backend.ticketapp.passport.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class PrincipalNameAuthentication implements Authentication {

    private final String principalName;

    public PrincipalNameAuthentication(String principalName) {
        Assert.hasText(principalName, "principalName cannot be empty");
        this.principalName = principalName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        throw unsupported();
    }

    @Override
    public Object getCredentials() {
        throw unsupported();
    }

    @Override
    public Object getDetails() {
        throw unsupported();
    }

    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        throw unsupported();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw unsupported();
    }

    @Override
    public String getName() {
        return this.principalName;
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException("Not Supported");
    }
}