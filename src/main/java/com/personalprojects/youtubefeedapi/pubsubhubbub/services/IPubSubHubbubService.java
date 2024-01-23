package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import com.personalprojects.youtubefeedapi.subscription.Subscription;
import com.personalprojects.youtubefeedapi.subscription.UserSubscriptionDto;

/**
 * Handles REST calls made to PubSubHubbub for subscribing/unsubscribing to topics.
 */
public interface IPubSubHubbubService {
    void subscribe(String topicUrl, String callbackUrl);
    void subscribe(Subscription subscription);
    void subscribe(String userId, String subscriptionId, String topicUrl);
    void subscribe(UserSubscriptionDto subscription);
    void unsubscribe(String topicUrl, String callbackUrl);
    void unsubscribe(Subscription subscription);
    void unsubscribe(String userId, String subscriptionId, String topicUrl);
}
