package com.personalprojects.youtubefeedapi.notification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/notification-settings")
public class NotificationSettingsController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationSettingsController.class);
    private final INotificationSettingsService notificationSettingsService;

    @GetMapping
    public ResponseEntity<NotificationSettingsDto> getNotificationSettings(@PathVariable String userId) {
        logger.info("getNotificationSettings() called");
        return ResponseEntity.ok(notificationSettingsService.getByUserId(userId));
    }

    @PatchMapping
    public ResponseEntity<NotificationSettingsDto> patchNotificationSettings(
            @PathVariable String userId,
            @RequestBody NotificationSettingsRequest notificationSettingsRequest
    ) {
        logger.info("patchNotificationSettings");
        return ResponseEntity.ok(notificationSettingsService.patchNotificationSettings(
                userId,
                notificationSettingsRequest
        ));
    }
}
