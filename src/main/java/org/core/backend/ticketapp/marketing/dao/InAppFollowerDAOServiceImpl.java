package org.core.backend.ticketapp.marketing.dao;

import org.core.backend.ticketapp.passport.dao.BaseDao;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.dtos.follower.InAppFollowerResponseDTO;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Service
public class InAppFollowerDAOServiceImpl extends BaseDao implements InAppFollowerDAOService {

    @Autowired
    DataSource dataSource;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public Page<InAppFollowerResponseDTO> filterSearch(FilterInAppFollowerRequestDTO request) {
        return null;
    }
}
