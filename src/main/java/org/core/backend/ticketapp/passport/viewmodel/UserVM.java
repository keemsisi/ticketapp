package org.core.backend.ticketapp.passport.viewmodel;

import lombok.Data;

import java.util.Date;

@Data
public class UserVM {
    
    long id, departmentId, unitId, positionId;

    String email, firstName, middleName, lastName, profilePictureLocation, phone, gender, occupation, maritalStatus, address, country, city, postalCode, stateOfOrigin, lgaOfOrigin, jobDescription, department, unit, position, chn, accountNumber;

    Date dob;   

    boolean locked, deactivated;
}
