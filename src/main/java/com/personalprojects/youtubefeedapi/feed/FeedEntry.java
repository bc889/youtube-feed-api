package com.personalprojects.youtubefeedapi.feed;


import com.personalprojects.youtubefeedapi.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeedEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long feedEntryId;

    private String title;

    private String videoId;

    private String channelId;

    private String channelName;

    private String channelUri;

    private String link;

    private LocalDateTime published;

    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}
