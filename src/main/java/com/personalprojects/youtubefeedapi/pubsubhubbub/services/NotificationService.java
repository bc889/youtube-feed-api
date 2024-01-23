package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import com.personalprojects.youtubefeedapi.config.properties.NotificationProperties;
import com.personalprojects.youtubefeedapi.pubsubhubbub.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * This service sends the notifications we get from pubsub subscriptions to our device.
 */
@Component
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationProperties notificationProperties;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private void sendPost(NotificationRequest request) {

        logger.info("SENDING REQUEST. Message: %s ".formatted(request.getMessage()));
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<NotificationRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.exchange(notificationProperties.getUri(), HttpMethod.POST, entity, Object.class);
    }

    @Override
    public void sendNotification(String message) {
        sendPost(NotificationRequest.builder()
                .message(message)
                .token(notificationProperties.getToken())
                .user(notificationProperties.getUser()).build()
        );
    }

    @Override
    public void sendNotification(String message, String title) {
        sendPost(NotificationRequest.builder()
                .message(message)
                .title(title)
                .token(notificationProperties.getToken())
                .user(notificationProperties.getUser()).build()
        );
    }

    @Override
    public void sendNotification(String message, String title, String token, String user) {
        sendPost(NotificationRequest.builder()
                .message(message)
                .token(token)
                .user(user)
                .title(title)
                .build()
        );
    }
}
