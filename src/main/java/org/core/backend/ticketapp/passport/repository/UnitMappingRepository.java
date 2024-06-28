package org.core.backend.ticketapp.passport.repository;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UnitMappingRepository {

    public String queryUnitById(UUID id) {
        return String.format("SELECT uu.id as id,uu.name as name,uu.department_id as department_id, dd.name as department_name" +
                " FROM unit uu "
                + "INNER JOIN user_department dd ON uu.department_id = dd.id " +
                " WHERE uu.id = %s", id);
    }

    public String queryUnitByName(String name) {
        return String.format("SELECT uu.id as id,uu.name as name,uu.department_id as department_id, dd.name as department_name" +
                " FROM unit uu "
                + " INNER JOIN user_department dd ON uu.department_id = dd.id " +
                " WHERE uu.name = %s", name);
    }

    public String queryUnits() {
        return "SELECT uu.id as id,uu.name as name,uu.department_id as department_id, dd.name as department_name" +
                " FROM unit uu "
                + "INNER JOIN user_department dd ON uu.department_id = dd.id";
    }
}
