package org.core.backend.ticketapp.passport.entity;


import lombok.Data;
import org.core.backend.ticketapp.common.util.BooleanToIntConverter;

import java.util.UUID;

import javax.persistence.*;


@Entity
@Table(name = "email_verification")
@Data
public class EmailVerification {

    @Id
    @Column(name = "id")
    UUID id;

    @Column(name = "user_id")
    UUID userId;

    @Column(name = "email_address")
    String emailAddress;

    @Column(name = "token")
    String token;

    @Column(name = "date_issued")
    long dateIssued;

    @Column(name = "expiry_date")
    long expiryDate;

    @Convert(converter = BooleanToIntConverter.class)
    boolean tokenUsed;
}
