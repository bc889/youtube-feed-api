package com.personalprojects.youtubefeedapi.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface INotificationSettingsRepository extends JpaRepository<NotificationSettings, Long> {

    @Query("SELECT n FROM NotificationSettings n WHERE n.user.userId = :userId")
    Optional<NotificationSettings> findByUserId(String userId);
}
