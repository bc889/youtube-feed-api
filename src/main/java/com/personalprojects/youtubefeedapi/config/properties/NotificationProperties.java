package com.personalprojects.youtubefeedapi.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "notification")
public class NotificationProperties {

    String uri;
    String token;
    String user;
}
