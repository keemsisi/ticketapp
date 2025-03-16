package org.core.backend.ticketapp.marketing.dto.social;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.SocialMedia;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSocialLinksRequest {
    public List<Media> media;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static final class Media {
        @NotNull
        @NotBlank
        private String profileLink;
        @NotNull
        @NotBlank
        private String handle;
        @NotNull
        private SocialMedia socialMedia;
    }
}
