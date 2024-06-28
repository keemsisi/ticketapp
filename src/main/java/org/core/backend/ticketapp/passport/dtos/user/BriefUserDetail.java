package org.core.backend.ticketapp.passport.dtos.user;

import lombok.Data;

import java.util.UUID;

@Data
public class BriefUserDetail {
    UUID id;
    String firstname;
    String lastname;
    String status = "Active";
}