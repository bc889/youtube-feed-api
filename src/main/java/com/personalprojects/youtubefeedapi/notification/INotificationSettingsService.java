package com.personalprojects.youtubefeedapi.notification;

public interface INotificationSettingsService {

    NotificationSettingsDto patchNotificationSettings(String userId, NotificationSettingsRequest request);

    NotificationSettingsDto getByUserId(String userId);
}
