package org.core.backend.ticketapp.passport.dtos.core;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class GroupDto {
        @NotBlank
        private String name;
        @NotBlank
        private String code;
        @NotBlank
        private String description;
        private List<GroupActionsDto> actions;
        private List<UserAvatar> users;
}
