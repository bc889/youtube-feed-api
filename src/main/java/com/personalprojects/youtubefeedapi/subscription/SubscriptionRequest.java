package com.personalprojects.youtubefeedapi.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {

    private String name;
    private String topicUrl;
    private Boolean allUploadActivity;
}
