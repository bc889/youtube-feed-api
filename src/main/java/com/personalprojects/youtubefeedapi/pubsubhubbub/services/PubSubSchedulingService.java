package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import com.personalprojects.youtubefeedapi.config.properties.NgrokProperties;
import com.personalprojects.youtubefeedapi.subscription.ISubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This service sends out our pubsub subscriptions, as well as refreshes them.
 */
@Service
@ConditionalOnProperty(name = "application.disable-scheduler", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class PubSubSchedulingService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PubSubSchedulingService.class);

    private final IPubSubHubbubService pubSubHubbubService;

    private final ISubscriptionService subscriptionService;

    private final NgrokProperties ngrokProperties;

    private void doSubscriptions() {
        logger.info("PubSubSchedulingService.doSubscriptions() executed at: " + System.currentTimeMillis());

        subscriptionService.getAllUserSubscriptions().stream()
            .filter(sub -> sub.getUserId() != null)
            .forEach(pubSubHubbubService::subscribe);

//        topicUrls.forEach(topic -> {
//            logger.info("unsubbing to " + topic);
//            pubSubHubbubService.unsubscribe(topic, callbackUrl);
//        });
    }

    @Scheduled(fixedDelay = 3600000 ) // Use the property or default to 1 hour
    public void doScheduledResubs() {
        logger.info("PubSubSchedulingService: Handling resubs...");
        doSubscriptions();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("PubSubSchedulingService: baseUrl=" + ngrokProperties.getBaseUrl());
        logger.info("PubSubSchedulingService initialized: handling subscriptions...");
        doSubscriptions();
    }
}
