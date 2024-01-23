package com.personalprojects.youtubefeedapi.subscription;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/subscriptions")
public class SubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    private final ISubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAll(@PathVariable String userId) {
        logger.info("'getAll()' called");
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(userId));
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionDto> getById(@PathVariable String userId, @PathVariable Long subscriptionId) {
        logger.info("'geById()' called");
        return ResponseEntity.ok(subscriptionService.getByUserIdAndSubscriptionId(userId, subscriptionId));
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(
            @PathVariable String userId,
            @RequestBody SubscriptionRequest subscriptionRequest) {
        logger.info("'post()' called, Subscription: {}", subscriptionRequest);
        return ResponseEntity.ok(subscriptionService.createSubscription(userId, subscriptionRequest));
    }

    @PostMapping("/load")
    public ResponseEntity<List<SubscriptionDto>> createSubscriptions(
            @PathVariable String userId,
            @RequestBody List<SubscriptionRequest> subscriptionRequest) {
        logger.info("'post()' called, Subscriptions: {}", subscriptionRequest);
        return ResponseEntity.ok(subscriptionService.createSubscriptions(userId, subscriptionRequest));
    }

    // TODO: return a string
    @DeleteMapping("/{subscriptionId}")
    public void delete(@PathVariable String userId, @PathVariable Long subscriptionId) {
        logger.info("'delete()' called, subscriptionId: {}", subscriptionId);
        subscriptionService.deleteSubscription(userId, subscriptionId);
    }

    @PatchMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionDto> patch(
            @PathVariable String userId,
            @PathVariable Long subscriptionId,
            @RequestBody SubscriptionRequest subscriptionRequest) throws Exception {
        logger.info("'patch()' called, subscriptionId: {}", subscriptionRequest);
        return ResponseEntity.ok(subscriptionService.patchSubscription(userId, subscriptionId, subscriptionRequest));
    }
}
