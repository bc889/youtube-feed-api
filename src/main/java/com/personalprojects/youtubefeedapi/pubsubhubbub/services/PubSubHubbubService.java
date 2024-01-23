package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import com.personalprojects.youtubefeedapi.config.properties.NgrokProperties;
import com.personalprojects.youtubefeedapi.subscription.Subscription;
import com.personalprojects.youtubefeedapi.subscription.UserSubscriptionDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PubSubHubbubService implements IPubSubHubbubService {

    private static final Logger logger = LoggerFactory.getLogger(PubSubHubbubService.class);
    private static final String hubUrl = "https://pubsubhubbub.appspot.com/subscribe";

    private final IVerificationTokenService verificationTokenService;

    private final NgrokProperties ngrokProperties;

    @PostConstruct
    public void init() {
        logger.info("PubSubHubbubService initialized");
    }

    @Override
    public void subscribe(String topicUrl, String callbackUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String token = verificationTokenService.getVerificationToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("hub.mode", "subscribe");
        urlParams.put("hub.topic", topicUrl);
        urlParams.put("hub.callback", callbackUrl);
        urlParams.put("hub.verify", "sync");
        urlParams.put("hub.verify_token", token);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.setAll(urlParams);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        logger.debug("Sending subscribe request " + request);

        try {
            ResponseEntity<String> response = restTemplate.exchange(hubUrl, HttpMethod.POST, request, String.class);
            logger.info("Subscribe Response: " + response.getBody());
        }
        catch (Exception e) {
            logger.error("Error Subscribing: ", e);
        }
    }

    @Override
    public void subscribe(Subscription subscription) {
        String topic = subscription.getTopicUrl();
        String userId = subscription.getUser().getUserId();
        String subscriptionId = String.valueOf(subscription.getSubscriptionId());
        String userCallbackUrl = getSubscriptionCallbackUrl(userId, subscriptionId);

        logger.info("subbing to " + topic);
        logger.info("callback: " + userCallbackUrl);

        this.subscribe(topic, userCallbackUrl);
    }

    @Override
    public void subscribe(String userId, String subscriptionId, String topicUrl) {
        String userCallbackUrl = getSubscriptionCallbackUrl(userId, subscriptionId);

        logger.info("subbing to " + topicUrl);
        logger.info("callback: " + userCallbackUrl);

        this.subscribe(topicUrl, userCallbackUrl);
    }

    @Override
    public void subscribe(UserSubscriptionDto subscription) {
        String topic = subscription.getTopicUrl();
        String userId = subscription.getUserId();
        String subscriptionId = subscription.getSubscriptionId().toString();
        String userCallbackUrl = getSubscriptionCallbackUrl(userId, subscriptionId);

        logger.info("subbing to " + topic);
        logger.info("callback: " + userCallbackUrl);

        this.subscribe(topic, userCallbackUrl);
    }

    @Override
    public void unsubscribe(String topicUrl, String callbackUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String token = verificationTokenService.getVerificationToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("hub.mode", "unsubscribe");
        urlParams.put("hub.topic", topicUrl);
        urlParams.put("hub.callback", callbackUrl);
        urlParams.put("hub.verify", "sync");
        urlParams.put("hub.verify_token", token);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.setAll(urlParams);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        // This has our token for pubsub, keep it in debug level
        logger.debug("Sending unsubscribe request " + request);

        try {
            ResponseEntity<String> response = restTemplate.exchange(hubUrl, HttpMethod.POST, request, String.class);
            logger.info("Unsubscribe Response: " + response.getBody());
        }
        catch (Exception e) {
            logger.error("Error Unsubscribing: ", e);
        }
    }

    @Override
    public void unsubscribe(Subscription subscription) {
        String topic = subscription.getTopicUrl();
        String userId = subscription.getUser().getUserId();
        String subscriptionId = String.valueOf(subscription.getSubscriptionId());
        String userCallbackUrl = getSubscriptionCallbackUrl(userId, subscriptionId);

        logger.info("unsubbing to " + topic);
        this.unsubscribe(topic, userCallbackUrl);
    }

    @Override
    public void unsubscribe(String userId, String subscriptionId, String topicUrl) {
        String userCallbackUrl = getSubscriptionCallbackUrl(userId, subscriptionId);
        logger.info("unsubbing to " + topicUrl);
        this.unsubscribe(topicUrl, userCallbackUrl);
    }


    private String getSubscriptionCallbackUrl(String userId, String subscriptionId) {
        return UriComponentsBuilder.fromUriString(ngrokProperties.getBaseUrl())
                .pathSegment("users", userId, "subscriptions", subscriptionId, "push")
                .toUriString();
    }
}
