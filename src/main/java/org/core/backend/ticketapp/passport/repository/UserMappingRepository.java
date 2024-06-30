package org.core.backend.ticketapp.passport.repository;

import org.springframework.stereotype.Component;

@Component
public class UserMappingRepository {

    public String queryProfile(String email) {
    			
        String query = "SELECT users.*, department.name AS department FROM users"+
		" WHERE users.email = '"+ email +"'";
        
        return query;
    }
}
