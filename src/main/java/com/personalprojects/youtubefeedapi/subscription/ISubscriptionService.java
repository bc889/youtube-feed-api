package com.personalprojects.youtubefeedapi.subscription;

import java.util.List;

public interface ISubscriptionService {
    List<SubscriptionDto> getAllSubscriptions(String userId);

    SubscriptionDto getByUserIdAndSubscriptionId(String userId, Long subscriptionId);

    SubscriptionDto createSubscription(String userId, SubscriptionRequest subscriptionRequest);

    List<SubscriptionDto> createSubscriptions(String userId, List<SubscriptionRequest> subscriptionRequest);

    void deleteSubscription(String userId, Long subscriptionId);

    SubscriptionDto patchSubscription(
            String userId,
            Long subscriptionId,
            SubscriptionRequest subscriptionRequest
    ) throws Exception;

    List<UserSubscriptionDto> getAllUserSubscriptions();
}
