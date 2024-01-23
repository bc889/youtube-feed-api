package com.personalprojects.youtubefeedapi.subscription.admin;

import com.personalprojects.youtubefeedapi.subscription.ISubscriptionService;
import com.personalprojects.youtubefeedapi.subscription.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users/{userId}/subscriptions")
public class SubscriptionAdminController {
    private static final Logger logger = LoggerFactory.getLogger(SubscriptionAdminController.class);

    private final ISubscriptionService subscriptionService;

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionDto> getById_ADMIN(@PathVariable String userId, @PathVariable Long subscriptionId) {
        return ResponseEntity.ok(subscriptionService.getByUserIdAndSubscriptionId(userId, subscriptionId));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAll_ADMIN(@PathVariable String userId) {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(userId));
    }
}
