package org.core.backend.ticketapp.passport.repository;

import org.springframework.stereotype.Component;

@Component
public class UserMappingRepository {

    public String queryProfile(String email) {
    			
        String query = "SELECT users.*, department.name AS department, unit.name AS unit FROM users"+
		" LEFT JOIN department ON users.department_id = department.id "+
		" LEFT JOIN unit ON users.unit_id = unit.id "+
		" WHERE users.email = '"+ email +"'";
        
        return query;
    }
}
