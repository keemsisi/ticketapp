package org.core.backend.ticketapp.passport.dtos.group;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CreateGroupDTO {
    @NotBlank
    private String name;
    @NotNull
    private String description;
    private List<UUID> actionIds = new ArrayList<>();
    private List<UUID> userIds = new ArrayList<>();
}