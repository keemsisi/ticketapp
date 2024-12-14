package org.core.backend.ticketapp.passport.dao;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Transactional
public class QrCodeDAOImpl extends BaseDao implements QrCodeDAOService {
    private final DataSource dataSource;
    private final JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }


    @Override
    @SuppressWarnings("unchecked")
    public Page<QrCode> getAll(final UUID tenantId, final UUID userId, final FilterTicketRequestDTO prp, final Pageable pageable) {
        assert getJdbcTemplate() != null;
        final String order = "DESC";
        final var limit = pageable.getPageSize();
        int SIZE = ObjectUtils.isEmpty(pageable.getPageSize()) ? 50 : pageable.getPageSize();
        int PAGE = pageable.getPageNumber();
        final var skip = PAGE * SIZE;

        final var FIELDS = """
                       qr.*, e.title as event_name,
                       e.event_banner as event_banner,
                       CONCAT(u.first_name, ' ', u.middle_name, ' ', u.last_name) as owner_name
                """;
        String query = """
                SELECT :fields FROM qr_code qr
                INNER JOIN event e ON e.id = qr.event_id
                INNER JOIN users u ON u.id = qr.user_id AND qr.tenant_id = u.tenant_id
                WHERE qr.tenant_id IS NOT NULL :subQuery
                :order
                """;
        final var subQuery = new StringBuilder();
        final var paginationQuery = String.format(" ORDER BY e.date_created %s OFFSET %s LIMIT %s ", order, skip, limit);

        if (ObjectUtils.isNotEmpty(prp.eventId())) {
            subQuery.append(String.format(" AND e.id='%s' ", prp.eventId()));
        }
        if (ObjectUtils.isNotEmpty(userId)) {
            subQuery.append(String.format(" AND qr.user_id='%s' ", userId));
        }
        subQuery.append(String.format(" AND qr.tenant_id='%s' ", tenantId));

        final var mainQuery = query.replaceAll(":subQuery", subQuery.toString())
                .replaceAll(":fields", FIELDS)
                .replace(":order", paginationQuery);
        final var countQuery = query.replaceAll(":subQuery", subQuery.toString())
                .replaceAll(":fields", "COUNT(*)")
                .replaceAll(":order", "");
        final var finalQuery = String.format("%s;%s", mainQuery, countQuery);
        var cscFactory = new CallableStatementCreatorFactory(finalQuery);
        final var returnedParams = Arrays.<SqlParameter>asList(
                new SqlReturnResultSet("qrcode", BeanPropertyRowMapper.newInstance(QrCode.class)),
                new SqlReturnResultSet("count", BeanPropertyRowMapper.newInstance(LongWrapper.class)));
        final var csc = cscFactory.newCallableStatementCreator(new HashMap<>());
        final var results = getJdbcTemplate().call(csc, returnedParams);
        final var pagedResults = new Page<QrCode>();
        final var codes = (List<QrCode>) results.get("qrcode");
        final var counts = ((ArrayList<LongWrapper>) results.get("count")).get(0).getCount();
        pagedResults.setContent(codes);
        pagedResults.setCount(counts);
        pagedResults.setSize(codes.size());
        pagedResults.setPageNumber(PAGE);
        pagedResults.setReqSize(SIZE);
        pagedResults.setLast(codes.isEmpty());
        pagedResults.setNumberOfElements(codes.size());
        pagedResults.setTotalElements(counts);
        return pagedResults;
    }
}
