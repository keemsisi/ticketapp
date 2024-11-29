package org.core.backend.ticketapp.event.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.enums.TimeZoneEnum;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.UUID;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseRowMapper implements RowMapper<EventResponseDTO> {
    private ObjectMapper objectMapper;

    @Override
    public EventResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        EventResponseDTO eventResponseDTO = new EventResponseDTO();
        final HashSet<String> categoriesObj;
        try {
            categoriesObj = objectMapper.readValue(rs.getString("categories"), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        eventResponseDTO.setUserId((UUID) rs.getObject("user_id"));
        eventResponseDTO.setModifiedBy((UUID) rs.getObject("modified_by"));
        eventResponseDTO.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
        eventResponseDTO.setDateCreated(rs.getTimestamp("date_modified") == null ?
                null : rs.getTimestamp("date_modified").toLocalDateTime());
        eventResponseDTO.setIndex(rs.getLong("index"));
        eventResponseDTO.setDeleted(rs.getBoolean("deleted"));
        eventResponseDTO.setVersion(rs.getLong("version"));
        eventResponseDTO.setTenantId((UUID) rs.getObject("tenant_id"));
        eventResponseDTO.setId((UUID) rs.getObject("id"));
        eventResponseDTO.setTitle(rs.getString("title"));
        eventResponseDTO.setDescription(rs.getString("description"));
        eventResponseDTO.setPhysicalEvent(rs.getBoolean("physical_event"));
        eventResponseDTO.setFreeEvent(rs.getBoolean("free_event"));
        eventResponseDTO.setTicketsAvailable(rs.getInt("tickets_available"));
        eventResponseDTO.setMaxPerUser(rs.getInt("max_per_user"));
        eventResponseDTO.setLocation(rs.getString("location"));
        eventResponseDTO.setLocationNumber(rs.getString("location_number"));
        eventResponseDTO.setStreetAddress(rs.getString("street_address"));
        eventResponseDTO.setCategories(categoriesObj);
        eventResponseDTO.setEventBanner(rs.getString("event_banner"));
        eventResponseDTO.setTheme(rs.getString("theme"));
        eventResponseDTO.setRecurring(rs.getBoolean("recurring"));
        eventResponseDTO.setTimeZone(rs.getString("time_zone"));
        eventResponseDTO.setEventDate(rs.getTimestamp("event_date").toLocalDateTime());
        eventResponseDTO.setApprovalStatus(ApprovalStatus.valueOf(rs.getString("approval_status")));
        eventResponseDTO.setTicketType(EventTicketType.valueOf(rs.getString("ticket_type")));
        eventResponseDTO.setApprovalRequired(rs.getBoolean("approval_required"));
        eventResponseDTO.setEventWishListId((UUID) rs.getObject("event_wishlist_id"));
        return eventResponseDTO;
    }
}
