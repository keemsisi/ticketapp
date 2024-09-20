package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface VirtualEventService {
//  String createLink(Event event, LoggedInUserDto loggedInUserDto) throws Exception;
    String createLinkWithCalendar(Event event, String ownerEmail) throws Exception;

    void updateCalendarEvent(String eventId, Event request, List<String> attendees)
            throws GeneralSecurityException, IOException;

    void deleteCalendarEvent(String eventId) throws Exception;
}
