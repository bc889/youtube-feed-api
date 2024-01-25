package com.personalprojects.youtubefeedapi.pubsubhubbub;

import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.Author;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomEntry;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomFeed;
import com.personalprojects.youtubefeedapi.pubsubhubbub.services.AtomFeedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class AtomFeedServiceTest {

    private AtomFeedService atomFeedService;

    @BeforeEach
    public void setup() {
        this.atomFeedService = new AtomFeedService();
    }

    @Test
    public void givenValidXML_whenToAtomFeed_thenAtomFeedShouldHaveExpectedValues()
            throws IOException, JAXBException, DatatypeConfigurationException {
        // Arrange
        String xml = readTextFile("valid_notification.xml");

        // Act
        YTAtomFeed atomFeed = atomFeedService.toAtomFeed(xml);

        // Assert
        assertEquals("https://www.youtube.com/xml/feeds/videos.xml?channel_id=UCq6VFHwMzcMXbuKyG7SQYIg",
                atomFeed.getSelfLink().getHref());
        assertEquals("YouTube video feed", atomFeed.getTitle());
        assertEquals(calendarValue("2023-12-25T16:00:40.261758523+00:00"), atomFeed.getUpdated());

        YTAtomEntry entry = atomFeed.getEntry();
        assertEquals("yt:video:jo2XauADeNY", entry.getId());
        assertEquals("jo2XauADeNY", entry.getVideoId());
        assertEquals("UCq6VFHwMzcMXbuKyG7SQYIg", entry.getChannelId());
        assertEquals("The Official Podcast Episode #343: Sealing Away the Ocean", entry.getTitle());
        assertEquals("https://www.youtube.com/watch?v=jo2XauADeNY", entry.getLink().getHref());
        assertEquals(calendarValue("2023-12-10T21:00:21+00:00"), entry.getPublished());
        assertEquals(calendarValue("2023-07-02T16:00:40.261758523+00:00"), entry.getUpdated());

        Author author = entry.getAuthor();
        assertEquals("penguinz0", author.getName());
        assertEquals("https://www.youtube.com/channel/UCq6VFHwMzcMXbuKyG7SQYIg", author.getUri());
    }

    @Test
    public void givenInvalidValidXML_whenToAtomFeed_thenServiceShouldThrowJAXBException() throws IOException {
        // Arrange
        var xmlPayload = readTextFile("invalid_notification.xml");

        // Act & Assert
        assertThrows(JAXBException.class, () -> {
            atomFeedService.toAtomFeed(xmlPayload);
        });
    }

    private String readTextFile(String fileName) throws IOException {
        Path filePath = Paths.get("src", "test", "resources", fileName);
        return Files.readString(filePath);
    }

    private XMLGregorianCalendar calendarValue(String stringValue) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(stringValue);
    }
}
