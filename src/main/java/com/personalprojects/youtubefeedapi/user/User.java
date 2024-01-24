package com.personalprojects.youtubefeedapi.user;

import com.personalprojects.youtubefeedapi.feed.FeedEntry;
import com.personalprojects.youtubefeedapi.notificationsettings.NotificationSettings;
import com.personalprojects.youtubefeedapi.subscription.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private String userId;

    @OneToOne(
            targetEntity = NotificationSettings.class,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private NotificationSettings notificationSettings;

    @OneToMany(targetEntity = Subscription.class, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscriptions;

    @OneToMany(targetEntity = Subscription.class, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FeedEntry> feed;
}
