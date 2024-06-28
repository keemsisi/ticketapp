package org.core.backend.ticketapp.passport.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemAlertDto {
    private boolean emailAlert;
    private boolean smsAlert;
}
