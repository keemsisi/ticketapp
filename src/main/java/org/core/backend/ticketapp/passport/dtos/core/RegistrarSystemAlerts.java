package org.core.backend.ticketapp.passport.dtos.core;

import io.github.thecarisma.CopyProperty;
import lombok.Data;
import org.core.backend.ticketapp.common.util.BooleanToIntConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import java.util.UUID;

@Data
public class RegistrarSystemAlerts {

    @Id
    @Column(name = "id")
    private UUID id;

    @CopyProperty(ignore = true)
    @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "active_directory")
    private boolean activeDirectory;

    @Column(name = "email")
    @Convert(converter = BooleanToIntConverter.class)
    private boolean email;

    @Column(name = "sms")
    @Convert(converter = BooleanToIntConverter.class)
    private boolean sms;
}
