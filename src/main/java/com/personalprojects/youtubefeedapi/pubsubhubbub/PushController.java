package com.personalprojects.youtubefeedapi.pubsubhubbub;

import com.personalprojects.youtubefeedapi.pubsubhubbub.services.IPushFeedService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/subscriptions/{subscriptionId}/push")
@RequiredArgsConstructor
public class PushController {

    private final IPushFeedService pushFeedService;

    @GetMapping
    public ResponseEntity<String> verifyPushCallback(
            @PathVariable String userId,
            @PathVariable Long subscriptionId,
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.topic") String topic,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam(value = "hub.lease_seconds", required = false) String leaseSeconds
    ) {
        return ResponseEntity.ok().body(pushFeedService.verifyCallback(
                userId,
                subscriptionId,
                mode,
                topic,
                challenge,
                verifyToken,
                leaseSeconds
        ));
    }

    @PostMapping
    public ResponseEntity<Void> handlePushCallback(
            @PathVariable String userId,
            @PathVariable Long subscriptionId,
            @RequestBody String payload
    ) {
        pushFeedService.createFeedEntry(userId, subscriptionId, payload);
        return ResponseEntity.ok().build();
    }
}
