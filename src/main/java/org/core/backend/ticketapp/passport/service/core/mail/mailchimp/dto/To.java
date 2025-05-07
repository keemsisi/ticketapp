package org.core.backend.ticketapp.passport.service.core.mail.mailchimp.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class To {
    private String email;
    private String name;
}
