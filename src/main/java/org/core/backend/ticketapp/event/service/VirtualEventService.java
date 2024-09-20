package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;

public interface VirtualEventService {
//  String createLink(Event event, LoggedInUserDto loggedInUserDto) throws Exception;
    String createLinkWithCalendar(Event event, String ownerEmail) throws Exception;
}
