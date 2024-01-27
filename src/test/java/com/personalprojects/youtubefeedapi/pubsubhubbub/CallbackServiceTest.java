package com.personalprojects.youtubefeedapi.pubsubhubbub;

import com.personalprojects.youtubefeedapi.error.BadRequestException;
import com.personalprojects.youtubefeedapi.error.NotFoundException;
import com.personalprojects.youtubefeedapi.error.UnauthorizedException;
import com.personalprojects.youtubefeedapi.error.enums.BadRequestErrorCode;
import com.personalprojects.youtubefeedapi.error.enums.NotFoundErrorCode;
import com.personalprojects.youtubefeedapi.error.enums.UnauthorizedErrorCode;
import com.personalprojects.youtubefeedapi.feed.FeedEntry;
import com.personalprojects.youtubefeedapi.feed.IFeedEntryMapper;
import com.personalprojects.youtubefeedapi.feed.IFeedEntryRepository;
import com.personalprojects.youtubefeedapi.notificationsettings.INotificationSettingsRepository;
import com.personalprojects.youtubefeedapi.notificationsettings.NotificationSettings;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.Author;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.Link;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomEntry;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomFeed;
import com.personalprojects.youtubefeedapi.pubsubhubbub.services.CallbackService;
import com.personalprojects.youtubefeedapi.pubsubhubbub.services.IAtomFeedService;
import com.personalprojects.youtubefeedapi.pubsubhubbub.services.IPushoverService;
import com.personalprojects.youtubefeedapi.pubsubhubbub.services.IVerificationTokenService;
import com.personalprojects.youtubefeedapi.subscription.ISubscriptionRepository;
import com.personalprojects.youtubefeedapi.subscription.Subscription;
import com.personalprojects.youtubefeedapi.user.IUserRepository;
import com.personalprojects.youtubefeedapi.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CallbackServiceTest {

    @Mock
    private IPushoverService pushoverService;

    @Mock
    private IVerificationTokenService verificationTokenService;

    @Mock
    private INotificationSettingsRepository notificationSettingsRepository;

    @Mock
    private IAtomFeedService atomFeedService;

    @Mock
    private IFeedEntryMapper feedEntryMapper;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private IFeedEntryRepository feedEntryRepository;

    @InjectMocks
    private CallbackService callbackService;

    @Test
    public void verifyCallback_ValidToken_ReturnsChallenge() {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String mode = "subscribe";
        String topic = "https://example.com";
        String challenge = "testChallenge";
        String verifyToken = "testToken";
        String leaseSeconds = "3600";

        when(verificationTokenService.getVerificationToken()).thenReturn("testToken");

        // Act
        String result = callbackService.verifyCallback(userId, subscriptionId, mode, topic, challenge, verifyToken, leaseSeconds);

        // Assert
        assertEquals(challenge, result);
    }


    @Test
    public void createFeedEntry_ValidPayloadWithNoPriorUploadsAndUserEnabledNotifications_SavesAndSendsNotification()
            throws JAXBException, DatatypeConfigurationException {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String payload = "testPayload";
        User user = new User();
        Subscription subscription = new Subscription();
        FeedEntry feedEntry = new FeedEntry();
        NotificationSettings settings = NotificationSettings.builder()
                .enableNotifications(true)
                .pushoverToken("testToken")
                .pushoverUser("testUser")
                .build();

        YTAtomFeed atomFeed = createTestAtomFeed();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId))
                .thenReturn(Optional.of(subscription));
        when(atomFeedService.toAtomFeed(payload)).thenReturn(atomFeed);
        when(atomFeedService.isValidForSubscription(atomFeed, subscription)).thenReturn(true);
        when(atomFeedService.isRecentUpload(atomFeed.getEntry())).thenReturn(true);
        when(atomFeedService.isValidForSubscription(atomFeed,subscription)).thenReturn(true);
        when(feedEntryMapper.toFeedEntry(atomFeed.getEntry())).thenReturn(feedEntry);
        when(feedEntryRepository.findMostRecentByUserIdAndVideoId(userId, feedEntry.getVideoId()))
                .thenReturn(Optional.empty());
        when(notificationSettingsRepository.findByUserId(userId)).thenReturn(Optional.of(settings));

        // Act
        callbackService.createFeedEntry(userId, subscriptionId, payload);

        // Assert
        verify(feedEntryRepository, times(1)).save(any(FeedEntry.class));
        verify(pushoverService, times(1)).sendNotification(
                anyString(), anyString(), anyString(), anyString()
        );
    }

    // Exceptions
    @Test
    public void verifyCallback_InvalidToken_ThrowsInvalidTokenRequest() {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String mode = "subscribe";
        String topic = "https://example.com";
        String challenge = "testChallenge";
        String verifyToken = "invalidToken";
        String leaseSeconds = "3600";

        when(verificationTokenService.getVerificationToken()).thenReturn("testToken");

        // Act & Assert
        var exception = assertThrows(UnauthorizedException.class, () ->
                callbackService.verifyCallback(
                        userId,
                        subscriptionId,
                        mode,
                        topic,
                        challenge,
                        verifyToken,
                        leaseSeconds
                ));
        assertEquals(UnauthorizedErrorCode.INVALID_TOKEN_REQUEST, exception.getErrorCode());
    }

    @Test
    public void createFeedEntry_InvalidXMLPayload_ThrowsFeedParsingError() throws JAXBException {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String payload = "testPayload";
        User user = new User();
        Subscription subscription = new Subscription();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId))
                .thenReturn(Optional.of(subscription));
        when(atomFeedService.toAtomFeed(payload)).thenThrow(JAXBException.class);


        // Act & assert
        var exception = assertThrows(BadRequestException.class, () ->
                callbackService.createFeedEntry(userId, subscriptionId, payload)
        );
        assertEquals(BadRequestErrorCode.FEED_PARSING_ERROR, exception.getErrorCode());
    }

    @Test
    public void createFeedEntry_ValidPayloadWhereTopicDoesNotMatchSubscription_ThrowSubscriptionMismatch()
            throws JAXBException, DatatypeConfigurationException {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String payload = "testPayload";
        User user = new User();
        Subscription subscription = new Subscription();

        YTAtomFeed atomFeed = createTestAtomFeed();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId))
                .thenReturn(Optional.of(subscription));
        when(atomFeedService.toAtomFeed(payload)).thenReturn(atomFeed);
        when(atomFeedService.isValidForSubscription(atomFeed, subscription)).thenReturn(false);

        // Act & assert
        var exception = assertThrows(BadRequestException.class, () ->
                callbackService.createFeedEntry(userId, subscriptionId, payload)
        );
        assertEquals(BadRequestErrorCode.SUBSCRIPTION_MISMATCH, exception.getErrorCode());
    }

    @Test
    public void createFeedEntry_ValidPayloadThatHasInvalidDates_ThrowDateParsingError()
            throws JAXBException, DatatypeConfigurationException {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String payload = "testPayload";
        User user = new User();
        Subscription subscription = new Subscription();

        YTAtomFeed atomFeed = createTestAtomFeed();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId))
                .thenReturn(Optional.of(subscription));
        when(atomFeedService.toAtomFeed(payload)).thenReturn(atomFeed);
        when(atomFeedService.isValidForSubscription(atomFeed, subscription)).thenReturn(true);
        when(atomFeedService.isRecentUpload(atomFeed.getEntry()))
                .thenThrow(DatatypeConfigurationException.class);

        // Act & Assert
        var exception = assertThrows(BadRequestException.class, () ->
                callbackService.createFeedEntry(userId, subscriptionId, payload)
        );
        assertEquals(BadRequestErrorCode.DATE_PARSING_ERROR, exception.getErrorCode());
    }

    @Test
    public void createFeedEntry_userNotFound_ThrowUserIDNotFound() {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String payload = "testPayload";

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(NotFoundException.class, () ->
                callbackService.createFeedEntry(userId, subscriptionId, payload)
        );
        assertEquals(NotFoundErrorCode.USER_ID_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void createFeedEntry_subscriptionNotFound_ThrowSubscriptionNotFound() {
        // Arrange
        String userId = "testUser";
        Long subscriptionId = 1L;
        String payload = "testPayload";
        User user = new User();

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId))
                .thenReturn(Optional.empty());

        // Act & Assert
        var exception = assertThrows(NotFoundException.class, () ->
                callbackService.createFeedEntry(userId, subscriptionId, payload)
        );
        assertEquals(NotFoundErrorCode.SUBSCRIPTION_ID_NOT_FOUND, exception.getErrorCode());
    }

    private YTAtomFeed createTestAtomFeed() throws DatatypeConfigurationException {
        YTAtomFeed atomFeed = new YTAtomFeed();

        // Self link
        Link selfLink = new Link();
        selfLink.setHref("https://www.youtube.com/xml/feeds/videos.xml?channel_id=UCq6VFHwMzcMXbuKyG7SQYIg");
        atomFeed.setSelfLink(selfLink);

        // Title
        atomFeed.setTitle("YouTube video feed");

        // Updated
        atomFeed.setUpdated(calendarValue("2023-12-25T16:00:40.261758523+00:00"));

        // Entry
        YTAtomEntry entry = new YTAtomEntry();
        entry.setId("yt:video:jo2XauADeNY");
        entry.setVideoId("jo2XauADeNY");
        entry.setChannelId("UCq6VFHwMzcMXbuKyG7SQYIg");
        entry.setTitle("The Official Podcast Episode #343: Sealing Away the Ocean");

        // Entry Link
        Link entryLink = new Link();
        entryLink.setHref("https://www.youtube.com/watch?v=jo2XauADeNY");
        entry.setLink(entryLink);

        entry.setPublished(calendarValue("2023-12-10T21:00:21+00:00"));
        entry.setUpdated(calendarValue("2023-07-02T16:00:40.261758523+00:00"));

        // Entry Author
        Author author = new Author();
        author.setName("penguinz0");
        author.setUri("https://www.youtube.com/channel/UCq6VFHwMzcMXbuKyG7SQYIg");
        entry.setAuthor(author);

        atomFeed.setEntry(entry);

        return atomFeed;
    }

    private XMLGregorianCalendar calendarValue(String date) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
    }
}

