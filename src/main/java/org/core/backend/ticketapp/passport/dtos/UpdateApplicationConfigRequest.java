package org.core.backend.ticketapp.passport.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApplicationConfigType;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateApplicationConfigRequest {
    private UUID id;
    private Object data;
    private ApplicationConfigType type;
}
