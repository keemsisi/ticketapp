package org.core.backend.ticketapp.event.dao;

import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;

public class EventDao extends BaseDao {

    @Autowired
    DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    public List<Event> getEventByCategory(String category, String searchParam) {
        String sql = "SELECT * FROM event u WHERE ";
        switch (category.toLowerCase()) {
            case "free":
                sql += "price = 0";
                break;
            case "paid":
                sql += "price > 0";
                break;
            case "location":
                sql += "location ILIKE ?";
                break;
            case "description":
                sql += "description ILIKE ?";
                break;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }

        if (category.equals("location") || category.equals("description")) {
            return jdbcTemplate.query(sql, new Object[]{"%" + searchParam + "%"}, new BeanPropertyRowMapper<>(Event.class));
        } else {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Event.class));
        }
    }

}
