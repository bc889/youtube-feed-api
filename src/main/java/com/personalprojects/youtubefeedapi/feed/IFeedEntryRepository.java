package com.personalprojects.youtubefeedapi.feed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IFeedEntryRepository extends JpaRepository<FeedEntry, Long> {

    @Query("SELECT f FROM FeedEntry f WHERE f.user.userId = :userId")
    Page<FeedEntry> findByUserId(String userId, Pageable pageable);

    @Query("SELECT f FROM FeedEntry f WHERE f.user.userId = :userId AND f.feedEntryId = :feedEntryId")
    Optional<FeedEntry> findByUserIdAndFeedEntryId(String userId, Long feedEntryId);

    @Query("SELECT f FROM FeedEntry f WHERE f.user.userId = :userId")
    Page<FeedEntry> findByUserIdOrderByIdDesc(String userId, Pageable pageable);

    @Query("""
            SELECT fe FROM FeedEntry fe
                WHERE fe.user.userId = :userId
                AND fe.videoId = :videoId
                ORDER BY fe.updated DESC
                LIMIT 1
            """)
    Optional<FeedEntry> findMostRecentByUserIdAndVideoId(
            String userId,
            String videoId
    );
}
