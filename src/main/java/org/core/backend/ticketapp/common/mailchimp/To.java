package org.core.backend.ticketapp.common.mailchimp;

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
