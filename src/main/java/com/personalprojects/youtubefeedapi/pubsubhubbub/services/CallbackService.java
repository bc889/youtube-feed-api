package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

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
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.Author;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomEntry;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomFeed;
import com.personalprojects.youtubefeedapi.subscription.ISubscriptionRepository;
import com.personalprojects.youtubefeedapi.user.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CallbackService implements ICallbackService {
    private static final Logger logger = LoggerFactory.getLogger(CallbackService.class);

    private final IPushoverService pushoverService;

    private final IVerificationTokenService verificationTokenService;

    private final INotificationSettingsRepository notificationSettingsRepository;

    private final IAtomFeedService atomFeedService;

    private final IFeedEntryMapper feedEntryMapper;

    private final IUserRepository userRepository;

    private final ISubscriptionRepository subscriptionRepository;

    private final IFeedEntryRepository feedEntryRepository;

    @Override
    public String verifyCallback(String userId, Long subscriptionId, String mode, String topic, String challenge, String verifyToken, String leaseSeconds) {
        logger.info("Received verification request:");

        String storedToken = verificationTokenService.getVerificationToken();

        if (verifyToken.equals(storedToken)) {
            // Handle the verification request and return the challenge as the response
            logger.info("Mode: " + mode);
            logger.info("Topic: " + topic);
            logger.info("Challenge: " + challenge);
            logger.info("Lease Seconds: " + leaseSeconds);

            return challenge;
        }
        else {
            throw new UnauthorizedException(UnauthorizedErrorCode.INVALID_TOKEN_REQUEST);
        }
    }

    @Override
    public void createFeedEntry(String userId, Long subscriptionId, String payload) {

        logger.info("Received PuSH notification: " + payload);

        var user = userRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorCode.USER_ID_NOT_FOUND));

        var subscription = subscriptionRepository
                .findByUserIdAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new NotFoundException(NotFoundErrorCode.SUBSCRIPTION_ID_NOT_FOUND));

        try {
            YTAtomFeed atomFeed = atomFeedService.toAtomFeed(payload);

            // Is this notification actually for what we're subscribed to?
            if(!atomFeedService.isValidForSubscription(atomFeed, subscription))
                throw new BadRequestException(BadRequestErrorCode.SUBSCRIPTION_MISMATCH);

            YTAtomEntry atomEntry = atomFeed.getEntry();
            Author author = atomEntry.getAuthor();

            logger.info("Received Atom Entry:");
            logger.info(String.valueOf(atomEntry));

            if (subscription.isAllUploadActivity() || atomFeedService.isRecentUpload(atomEntry)) {
                FeedEntry entry = feedEntryMapper.toFeedEntry(atomEntry);
                entry.setUser(user);

                // Before saving, get our previous most recent update
                Optional<FeedEntry> prevMostRecentUpdate = feedEntryRepository
                        .findMostRecentByUserIdAndVideoId(userId, entry.getVideoId());

                // Add to the user's feed:
                feedEntryRepository.save(entry);
                logger.info("Entry saved.");

                if (prevMostRecentUpdate.isPresent()) {
                    // We've gotten an update for this video before, check to see if at least 2 minutes
                    // have elapsed before attempting another notification.
                    var prevEntry = prevMostRecentUpdate.get();
                    var duration = Duration.between(prevEntry.getUpdated(), entry.getUpdated());

                    if (duration.toMinutes() < 2) {
                        logger.info("Not enough time elapsed since last update, skipping notification: " + duration);
                        return;
                    }
                }

                // Attempt to send a notification:
                notificationSettingsRepository.findByUserId(userId).ifPresent(settings -> {
                    if (settings.isEnableNotifications()) {
                        var updatedString = prevMostRecentUpdate.isPresent() ? "(Video Updated) " : "";
                        pushoverService.sendNotification(
                                updatedString + atomEntry.getTitle() + "\n" + atomEntry.getLink().getHref(),
                                author.getName(),
                                settings.getPushoverToken(),
                                settings.getPushoverUser()
                        );
                    }
                });
            }
        } catch (JAXBException e) {
            throw new BadRequestException(BadRequestErrorCode.FEED_PARSING_ERROR);
        } catch (DatatypeConfigurationException e) {
            throw new BadRequestException(BadRequestErrorCode.DATE_PARSING_ERROR);
        }
    }
}
