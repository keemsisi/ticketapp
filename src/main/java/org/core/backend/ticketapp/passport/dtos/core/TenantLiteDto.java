package org.core.backend.ticketapp.passport.dtos.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.dto.SocialMediaDTO;
import org.core.backend.ticketapp.passport.validation.annotation.CountryCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TenantLiteDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String phone;
    @Email
    private String email;
    @CountryCode
    private String country;
    @NotBlank
    private String state;
    private UUID ownerId;
    private String websiteUrl;
    private List<String> galleries;
    private String logoUrl;
    private String phoneNumber;
    private String secondaryPhoneNumber;
    private String description;
    private List<SocialMediaDTO> socialMediaHandles;
    private List<String> services;
    private UUID userId;
}
