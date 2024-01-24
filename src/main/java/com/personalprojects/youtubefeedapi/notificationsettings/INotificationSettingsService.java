package com.personalprojects.youtubefeedapi.notificationsettings;

public interface INotificationSettingsService {

    NotificationSettingsDto patchNotificationSettings(String userId, NotificationSettingsRequest request);

    NotificationSettingsDto getByUserId(String userId);
}
