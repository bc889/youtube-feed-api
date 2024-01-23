package com.personalprojects.youtubefeedapi.feed;

import com.personalprojects.youtubefeedapi.error.NotFoundException;
import com.personalprojects.youtubefeedapi.error.enums.NotFoundErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedEntryService implements IFeedEntryService {

    private final IFeedEntryRepository feedEntryRepository;
    private final IFeedEntryMapper feedEntryMapper;

    @Override
    public Page<FeedEntryDto> getPaginatedFeed(String userId, Pageable pageable) {
        // Our ids are longs so ordering by descending id will get the latest first.
        return feedEntryMapper.toFeedEntryDto(feedEntryRepository.findByUserIdOrderByIdDesc(userId, pageable));
    }

    @Override
    public void deleteFeedEntry(String userId, Long feedEntryId) {
        Optional<FeedEntry> foundFeedEntry = feedEntryRepository.findByUserIdAndFeedEntryId(userId, feedEntryId);

        if (foundFeedEntry.isPresent()) {
            feedEntryRepository.deleteById(feedEntryId);
        }
        else {
            throw new NotFoundException(NotFoundErrorCode.FEED_ENTRY_ID_NOT_FOUND);
        }

    }
}
