package com.personalprojects.youtubefeedapi.notificationsettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingsDto {

    private Long notificationSettingsId;
    private Boolean enableNotifications;
    private String pushoverUser;
    private String pushoverToken;
}
