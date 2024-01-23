package com.personalprojects.youtubefeedapi.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingsRequest {

    private Boolean enableNotifications;
    private String pushoverUser;
    private String pushoverToken;
}
