package com.personalprojects.youtubefeedapi.feed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IFeedEntryService {

    Page<FeedEntryDto> getPaginatedFeed(String userId, Pageable pageable);

    void deleteFeedEntry(String userId, Long feedEntryId);

}
