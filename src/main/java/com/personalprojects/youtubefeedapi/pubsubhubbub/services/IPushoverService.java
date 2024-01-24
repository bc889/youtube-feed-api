package com.personalprojects.youtubefeedapi.pubsubhubbub.services;


/**
 * Handles sending notifications to users through Pushover.
 */
public interface IPushoverService {
    void sendNotification(String message, String title, String token, String user);
}