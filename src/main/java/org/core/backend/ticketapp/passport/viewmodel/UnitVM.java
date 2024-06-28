package org.core.backend.ticketapp.passport.viewmodel;

import lombok.Data;

import java.util.UUID;

@Data
public class UnitVM {
    UUID id;
    String name;
    UUID departmentId;
    String departmentName;
}
