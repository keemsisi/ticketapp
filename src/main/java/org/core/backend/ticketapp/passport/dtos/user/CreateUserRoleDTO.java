package org.core.backend.ticketapp.passport.dtos.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CreateUserRoleDTO {
    long roleId;
    List<Integer> userIds = new ArrayList<>();
}
