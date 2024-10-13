package org.core.backend.ticketapp.marketing.dto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSocialLinksRequest {
    private String ticketGlobal;
    private String instagram;
    private String tiktok;
    private String twitter;
    private String facebook;
    private String linkedIn;
    private String whatsApp;
}
