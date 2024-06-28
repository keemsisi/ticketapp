package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import java.util.List;

@Data
public class MultipleValuesRequest {

    private List<RoleDto> roles;
}
