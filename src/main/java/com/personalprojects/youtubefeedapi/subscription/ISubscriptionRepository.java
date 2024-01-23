package com.personalprojects.youtubefeedapi.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {
        //        List<Subscription> findByNameContainingIgnoreCase(String partialName);
        @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId")
        List<Subscription> findByUserId(String userId);

        @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND s.subscriptionId = :subscriptionId")
        Optional<Subscription> findByUserIdAndSubscriptionId(String userId, Long subscriptionId);

        @Query("SELECT s FROM Subscription s WHERE s.user.userId = :userId AND s.topicUrl = :topicUrl")
        Optional<Subscription> findByUserIdAndTopicUrl(String userId, String topicUrl);

        Optional<Subscription> findByTopicUrl(String topicUrl);
}
