package com.personalprojects.youtubefeedapi.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A dto for Subscriptions that contains no fields of any related entities.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {

    private Long subscriptionId;
    private String name;
    private String topicUrl;
    private Boolean allUploadActivity;
}
