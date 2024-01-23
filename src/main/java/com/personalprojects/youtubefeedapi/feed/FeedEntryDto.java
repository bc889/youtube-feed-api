package com.personalprojects.youtubefeedapi.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedEntryDto {

    private Long feedEntryId;
    private String title;
    private String videoId;
    private String channelId;
    private String channelName;
    private String channelUri;
    private String link;
    private LocalDateTime published;
    private LocalDateTime updated;
}
