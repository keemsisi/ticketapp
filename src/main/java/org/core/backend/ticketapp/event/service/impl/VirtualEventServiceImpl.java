package org.core.backend.ticketapp.event.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.auth.oauth2.ClientId;
import com.google.auth.oauth2.DefaultPKCEProvider;
import com.google.auth.oauth2.TokenStore;
import com.google.auth.oauth2.UserAuthorizer;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.entity.Event;
import org.core.backend.ticketapp.event.service.VirtualEventService;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VirtualEventServiceImpl implements VirtualEventService {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> CALENDAR_SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    private static final TokenStore TOKEN_STORE = new TokenStore() {
        private Path pathFor(String id) {
            return Paths.get(".", TOKENS_DIRECTORY_PATH, id + ".json");
        }

        @Override
        public String load(String id) throws IOException {
            if (!Files.exists(pathFor(id))) {
                return null;
            }
            return Files.readString(pathFor(id));
        }

        @Override
        public void store(String id, String token) throws IOException {
            Files.createDirectories(Paths.get(".", TOKENS_DIRECTORY_PATH));
            Files.writeString(pathFor(id), token);
        }

        @Override
        public void delete(String id) throws IOException {
            if (!Files.exists(pathFor(id))) {
                return;
            }
            Files.delete(pathFor(id));
        }
    };
    private final AppConfigs appConfigs;
    private final Calendar calendar;

    @Autowired
    public VirtualEventServiceImpl(final AppConfigs appConfigs) throws Exception {
        final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCalendarCredential(HTTP_TRANSPORT))
                .setApplicationName(appConfigs.appName)
                .build();
        this.appConfigs = appConfigs;
    }

    private static Credential getCalendarCredential(final HttpTransport HTTP_TRANSPORT) throws IOException {
        final var in = VirtualEventServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        final var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        final var flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, CALENDAR_SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static UserAuthorizer getAuthorizer(URI callbackUri, List<String> scopes) throws IOException {
        try (InputStream in = VirtualEventServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            final ClientId clientId = ClientId.fromStream(in);
            return UserAuthorizer.newBuilder()
                    .setClientId(clientId)
                    .setCallbackUri(callbackUri)
                    .setScopes(scopes)
                    .setPKCEProvider(new DefaultPKCEProvider() {
                        @Override
                        public String getCodeChallenge() {
                            return super.getCodeChallenge().split("=")[0];
                        }
                    })
                    .setTokenStore(TOKEN_STORE).build();
        }
    }

    @Override
    public String createLinkWithCalendar(final Event userEvent, String ownerEmail) {
        try {
            final var eventDate = userEvent.getEventDate();
            final var timeZone = userEvent.getTimeZone();
            final var event = new com.google.api.services.calendar.model.Event()
                    .setSummary(userEvent.getTitle())
                    .setLocation("Virtual")
                    .setDescription(userEvent.getDescription())
                    .setStart(new EventDateTime()
                            .setDateTime(DateTime.parseRfc3339(eventDate.toString()))
                            .setTimeZone(timeZone))
                    .setEnd(userEvent.isRecurring() ? null : new EventDateTime()
                            .setDateTime(DateTime.parseRfc3339(eventDate.plusDays(1).toString()))
                            .setTimeZone(timeZone))
                    .setAttendees(Collections.singletonList(new EventAttendee().setEmail(ownerEmail)));
            final var conferenceData = new ConferenceData()
                    .setCreateRequest(new CreateConferenceRequest()
                            .setRequestId(UUID.randomUUID().toString())
                            .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet")));
            event.setConferenceData(conferenceData);
            com.google.api.services.calendar.model.Event createdEvent = calendar.events()
                    .insert("primary", event)
                    .setConferenceDataVersion(1).execute();
            final var htmlLLink = createdEvent.getHtmlLink();
            final var link = createdEvent.getConferenceData().getEntryPoints().get(0).getUri();
            return link;
        } catch (final Exception e) {
            handException(e);
            return null;
        }
    }

    private void handException(final Exception e) {
        final var errorCode = System.currentTimeMillis();
        log.error(">>> [{}]Error occurred while creating meeting link", errorCode, e);
        throw new ApplicationException(400, "failed",
                String.format("Failed to create virtual event link, " +
                        "please try again later or contact support with code[%s]!", errorCode));
    }
}