package com.personalprojects.youtubefeedapi.pubsubhubbub;

import com.personalprojects.youtubefeedapi.pubsubhubbub.services.ICallbackService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/subscriptions/{subscriptionId}/push")
@RequiredArgsConstructor
public class CallbackController {

    private final ICallbackService callbackService;

    @GetMapping
    public ResponseEntity<String> verifyCallback(
            @PathVariable String userId,
            @PathVariable Long subscriptionId,
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.topic") String topic,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken,
            @RequestParam(value = "hub.lease_seconds", required = false) String leaseSeconds
    ) {
        return ResponseEntity.ok().body(callbackService.verifyCallback(
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
    public ResponseEntity<Void> handleCallback(
            @PathVariable String userId,
            @PathVariable Long subscriptionId,
            @RequestBody String payload
    ) {
        callbackService.createFeedEntry(userId, subscriptionId, payload);
        return ResponseEntity.ok().build();
    }
}
