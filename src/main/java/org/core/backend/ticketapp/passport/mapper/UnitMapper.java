package org.core.backend.ticketapp.passport.mapper;


import org.core.backend.ticketapp.passport.viewmodel.UnitVM;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitMapper implements RowMapper<UnitVM> {
    @Override
    public UnitVM mapRow(ResultSet rs, int arg1) throws SQLException {
        UnitVM unitVM = new UnitVM();
        unitVM.setName(rs.getString("name"));
        unitVM.setDepartmentName(rs.getString("department_Name"));
        return unitVM;
    }
}
