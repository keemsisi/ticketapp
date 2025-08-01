package org.core.backend.ticketapp.passport.dao;


import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.passport.entity.PricingSubscription;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class PricingSubscriptionDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Page<PricingSubscription> getAll(String name, Pageable pageable) {
        int limit = pageable.getPageSize();
        long offSet = pageable.getOffset();
        String sqlQ = String.format(name == null ? "SELECT * FROM pricing_subscription LIMIT " + limit + " OFFSET " + offSet + "; "
                : "SELECT * FROM pricing_subscription WHERE COALESCE(LOWER(name),'') LIKE '%" + name + "%'" + " \n" + " LIMIT " + limit + " OFFSET " + offSet + "; ");
        var cscFactory = new CallableStatementCreatorFactory(sqlQ + "SELECT COUNT(*) FROM pricing_subscription;");
        var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("pricingSubscription", BeanPropertyRowMapper.newInstance(PricingSubscription.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        CallableStatementCreator csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        Map<String, Object> results = jdbcTemplate.call(csc, returnedParams);
        return PageableExecutionUtils.getPage((List<PricingSubscription>) results.get("pricingSubscription"), pageable, () -> ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount());
    }
}