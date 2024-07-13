package org.core.backend.ticketapp.common.request.events.wishlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateEventWishListDTO {
    @NotNull(value = "Wished event is required!")
    private UUID eventId;
}
