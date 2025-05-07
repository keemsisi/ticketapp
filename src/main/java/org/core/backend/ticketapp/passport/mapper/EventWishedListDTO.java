package org.core.backend.ticketapp.passport.mapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowPrintStacktrace;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllowPrintStacktrace
public class EventWishedListDTO {
    private UUID id;
    private UUID eventId;
}
