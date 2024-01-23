package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

/**
 * Responsible for sending out notifications.
 */
public interface INotificationService {
    void sendNotification(String message);
    void sendNotification(String message, String title);
    void sendNotification(String message, String title, String token, String user);
}