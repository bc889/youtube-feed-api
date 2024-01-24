package com.personalprojects.youtubefeedapi.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "pushover")
public class PushoverProperties {

    String uri;
}
