package org.core.backend.ticketapp.passport.dao;

import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.passport.mapper.LongWrapper;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;

@Component
@Transactional
public class QrCodeDAOImpl extends BaseDao implements QrCodeDAOService {
    DataSource dataSource;
    JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    @SuppressWarnings("unchecked")
    public Page<QrCode> getAll(final UUID tenantId, final UUID userId, final FilterTicketRequestDTO prp, final Pageable pageable) {
        assert getJdbcTemplate() != null;
        int SIZE = ObjectUtils.isEmpty(pageable.getPageSize()) ? 50 : pageable.getPageSize();
        int PAGE = pageable.getPageNumber();

        final var FIELDS = """
                DISTINCT qr.*, e.name as event_name,
                       e.banner as event_banner,
                       CONCAT(u.first_name, ' ', u.middle_name, ' ', u.last_name) as owner_name
                """;
        String query = """
                SELECT :fields FROM qr_code qr
                INNER JOIN event e ON e.id = qr.event_id AND qr.tenant_id = e.tenant_id
                INNER JOIN user u ON u.id = qr.user_id AND qr.tenant_id = u.tenant_id
                WHERE qr.tenant_id IS NOT NULL :subQuery
                ORDER BY qr.date_created DESC
                """;
        final var subQuery = new StringBuilder();

        if (ObjectUtils.isNotEmpty(prp.eventId())) {
            subQuery.append(String.format(" AND e.id='%s' ", prp.eventId()));
        }
        subQuery.append(String.format(" AND qr.tenant_id='%s' ", tenantId));

        final var mainQuery = query.replaceAll(":subQuery", subQuery.toString()).replaceAll(":fields", FIELDS);
        final var countQuery = query.replaceAll(":subQuery", "COUNT(*)").replaceAll(":fields", FIELDS);
        final var finalQuery = String.format("%s;%s", mainQuery, countQuery);
        var cscFactory = new CallableStatementCreatorFactory(finalQuery);
        final var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("qrcode", BeanPropertyRowMapper.newInstance(QrCode.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        final var csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        final var results = getJdbcTemplate().call(csc, returnedParams);
        final var pagedResults = new Page<QrCode>();
        final var events = (List<QrCode>) results.get("qrcode");
        final var counts = ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount();
        pagedResults.setContent(events);
        pagedResults.setCount(counts);
        pagedResults.setSize(events.size());
        pagedResults.setPageNumber(PAGE);
        pagedResults.setReqSize(SIZE);
        pagedResults.setLast(events.isEmpty());
        pagedResults.setNumberOfElements(events.size());
        pagedResults.setTotalElements(counts);
        return pagedResults;
    }
}
