package org.core.backend.ticketapp.common.request;

import lombok.Data;
import org.core.backend.ticketapp.passport.dtos.RoleDto;

import java.util.List;


@Data
public class MultipleValuesRequest {

    private List<RoleDto> roles;
}
