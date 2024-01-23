package com.personalprojects.youtubefeedapi.feed;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/feed")
public class FeedController {

    private final IFeedEntryService feedEntryService;

    @GetMapping
    public Page<FeedEntryDto> getFeed(@PathVariable String userId, Pageable pageable) {
        return feedEntryService.getPaginatedFeed(userId, pageable);
    }

    @DeleteMapping("/{feedEntryId}")
    public void deleteFeedEntry(@PathVariable String userId, @PathVariable Long feedEntryId) {
        feedEntryService.deleteFeedEntry(userId, feedEntryId);
    }

}
