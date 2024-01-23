package com.personalprojects.youtubefeedapi.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A dto for Subscriptions that includes the userId
 * of the user that owns the subscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDto {

    private String userId;
    private Long subscriptionId;
    private String name;
    private String topicUrl;
    private Boolean allUploadActivity;
}
