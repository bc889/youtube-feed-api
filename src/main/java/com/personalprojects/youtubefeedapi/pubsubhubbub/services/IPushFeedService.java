package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

/**
 * Service for managing the callback endpoints that will be used by the Pubsubhubbub server
 */
public interface IPushFeedService {
    String verifyCallback(
            String userId,
            Long subscriptionId,
            String mode,
            String topic,
            String challenge,
            String verifyToken,
            String leaseSeconds
    );

    void createFeedEntry(String userId, Long subscriptionId, String payload);
}
