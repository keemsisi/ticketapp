package com.project.core.ticketapp.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AbstractBaseDAO {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
}
