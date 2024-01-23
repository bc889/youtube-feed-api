package com.personalprojects.youtubefeedapi.feed;

import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IFeedEntryMapper {

    FeedEntryDto toFeedEntryDto(FeedEntry feedEntry);

    List<FeedEntryDto> toFeedEntryDto(List<FeedEntry> entries);

    @Mapping(target = "channelName", source = "author.name")
    @Mapping(target = "channelUri", source = "author.uri")
    @Mapping(target = "link", source = "link.href")
    @Mapping(target = "published", source = "published", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "updated", source = "updated", qualifiedByName = "toLocalDateTime")
    FeedEntry toFeedEntry(YTAtomEntry atomEntry);

    default Page<FeedEntryDto> toFeedEntryDto(Page<FeedEntry> page) {
        return page.map(this::toFeedEntryDto);
    }

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }
}
