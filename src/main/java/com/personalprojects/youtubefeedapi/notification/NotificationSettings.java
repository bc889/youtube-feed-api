package com.personalprojects.youtubefeedapi.notification;

import com.personalprojects.youtubefeedapi.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notificationSettingsId;

    @Column(columnDefinition = "boolean default false")
    private boolean enableNotifications;

    private String pushoverUser;

    private String pushoverToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", unique = true)
    private User user;
}
