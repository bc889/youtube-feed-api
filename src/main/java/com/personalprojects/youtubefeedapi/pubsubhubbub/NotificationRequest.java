package com.personalprojects.youtubefeedapi.pubsubhubbub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private String token;
    private String user;
    private String message;
    private String title;
}
