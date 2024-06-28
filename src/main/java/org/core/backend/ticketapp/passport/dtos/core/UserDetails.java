package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.core.backend.ticketapp.common.util.Helpers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.*;

@Data
public class UserDetails implements Serializable {

    String email;

    Set<UserRoleDto> roles;

    Set<UserActionDto> actions;

    public UserDetails() {
        email = "";
        roles = new HashSet<>();
        actions = new HashSet<>();
    }

    public UserDetails(String email, Set<UserRoleDto> roles, Set<UserActionDto> actions) {
        this.email = email;
        this.roles = roles;
        this.actions = actions;
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        Object[] array = roles.toArray();
        for (Object role_ : array) {
            RoleDto roleDto = (role_ instanceof RoleDto) ? (RoleDto) role_ : new ObjectMapper().convertValue(role_, RoleDto.class);
            list.add(new SimpleGrantedAuthority(Helpers.RolePrefix + roleDto.getName().toUpperCase()));
        }
        return list;
    }

}
