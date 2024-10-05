package org.core.backend.ticketapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.SocialMedia;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialMediaDTO {
    private String link;
    private String handle;
    private SocialMedia socialMedia;
}
