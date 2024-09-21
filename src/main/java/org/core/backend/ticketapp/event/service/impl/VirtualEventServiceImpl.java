package org.core.backend.ticketapp.event.service.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class VirtualEventServiceImpl implements VirtualEventService {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens.json";
    private static final List<String> CALENDAR_SCOPES = List.of(CalendarScopes.CALENDAR);
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
    @Value("${system.google.api.credential.client_id}")
    private String clientId;
    @Value("${system.google.api.credential.client_secret}")
    private String clientSecret;
    @Value("${system.google.api.credential.refresh_token}")
    private String refreshToken;
    @Value("${system.google.api.credential.auth_code}")
    private String authCode;
    private Calendar calendar;

    @Autowired
    public VirtualEventServiceImpl(final AppConfigs appConfigs) throws Exception {
        final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        CompletableFuture.runAsync(() -> {
            try {
                this.calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCalendarCredential(HTTP_TRANSPORT))
                        .setApplicationName(appConfigs.appName)
                        .build();
            } catch (final Exception e) {
                log.error(">>>>Failed to get Authentication token for calendar", e);
            }
        });
        this.appConfigs = appConfigs;
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

    private Credential getCalendarCredential(final HttpTransport HTTP_TRANSPORT) throws IOException, GeneralSecurityException {
        return getCredentials();
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
            userEvent.setCalendarId(createdEvent.getId());
            return link;
        } catch (final Exception e) {
            handException(e);
            return null;
        }
    }

    @Override
    public void updateCalendarEvent(final String calendarId, final Event request, final List<String> attendees) throws IOException {
        final String calendarIdKey = "calendar-id";
        final var event = calendar.events().get(calendarIdKey, calendarId).execute();
        final var eventDate = request.getEventDate();
        final var timeZone = request.getTimeZone();
        if (event != null) {
            final var updatedEvent = new com.google.api.services.calendar.model.Event()
                    .setSummary(request.getTitle())
                    .setLocation(request.getLocation())
                    .setDescription(request.getDescription());
            final var startDateTime = new DateTime(eventDate.toString());
            final var start = new EventDateTime().setDateTime(startDateTime).setTimeZone(timeZone);
            updatedEvent.setStart(start);
            final var endDateTime = request.isRecurring() ? null : new EventDateTime()
                    .setDateTime(DateTime.parseRfc3339(eventDate.plusDays(1).toString()))
                    .setTimeZone(timeZone);
            updatedEvent.setEnd(endDateTime);
            final var eventAttendeeList = new ArrayList<EventAttendee>();
            attendees.forEach(eventAttendee -> eventAttendeeList.add(new EventAttendee().setEmail(eventAttendee)));
            final var eventAttendeeArray = eventAttendeeList.toArray(new EventAttendee[0]);
            updatedEvent.setAttendees(Arrays.asList(eventAttendeeArray));
            final var reminderOverrides =
                    new EventReminder[]{new EventReminder().setMethod("popup").setMinutes(2),};
            final var reminders = new com.google.api.services.calendar.model.Event.Reminders().setUseDefault(false).setOverrides(Arrays.asList(reminderOverrides));
            updatedEvent.setReminders(reminders);
            final var conferenceSolutionKey = new ConferenceSolutionKey().setType("hangoutsMeet");
            final var createConferenceRequest = new CreateConferenceRequest()
                    .setRequestId(request.getId().toString()).setConferenceSolutionKey(conferenceSolutionKey);
            final var conferenceData = new ConferenceData().setCreateRequest(createConferenceRequest);
            updatedEvent.setConferenceData(conferenceData);
            calendar.events().update(calendarIdKey, calendarId, updatedEvent).setSendNotifications(true).execute();
            log.info(">>> Event Calendar[{}] updated successfully!", calendarId);
        }
    }


    @Override
    public void deleteCalendarEvent(String eventId) throws Exception {
        final String calendarId = "calendar-id";
        final com.google.api.services.calendar.model.Event event = calendar.events()
                .get(calendarId, eventId).execute();
        if (event != null) {
            calendar.events().delete("calendar-id", eventId).setSendNotifications(true).execute();
        }
        log.info(">>> Successfully deleted event with calendarId : {} ", eventId);
    }

    private void handException(final Exception e) {
        final var errorCode = System.currentTimeMillis();
        log.error(">>> [{}]Error occurred while creating meeting link", errorCode, e);
        throw new ApplicationException(400, "failed",
                String.format("Failed to create virtual event link, " +
                        "please try again later or contact support with code[%s]!", errorCode));
    }

    private Credential getCredentials() throws IOException, GeneralSecurityException {
        final var decryptedRefreshToken = new String(Base64.getDecoder().decode(refreshToken));
        final var decryptedClientId = new String(Base64.getDecoder().decode(clientId));
        final var decryptedClientSecret = new String(Base64.getDecoder().decode(clientSecret));
        final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final var credential = new GoogleCredential.Builder()
                .setClientSecrets(decryptedClientId, decryptedClientSecret)
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .build();
        credential.setRefreshToken(decryptedRefreshToken);
        credential.refreshToken(); // Automatically refreshes the access token
        return credential;
    }


    private Credential getCredentialsInit() throws IOException, GeneralSecurityException {
        final var in = VirtualEventServiceImpl.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final var clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        final var flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, CALENDAR_SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        final var credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        log.info(">>> ACCESS TOKEN: [{}] REFRESH_TOKEN: [{}] EXPIRY: [{}] ", credential.getAccessToken(),
                credential.getRefreshToken(), credential.getExpiresInSeconds());
        return credential;
    }
}