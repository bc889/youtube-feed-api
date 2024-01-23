package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomEntry;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomFeed;
import com.personalprojects.youtubefeedapi.subscription.Subscription;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Service
public class AtomFeedService implements IAtomFeedService {

    @Override
    public YTAtomFeed toAtomFeed(String payload) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(YTAtomFeed.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(payload);

        return (YTAtomFeed) unmarshaller.unmarshal(reader);
    }

    @Override
    public boolean isRecentUpload(YTAtomEntry atomEntry) throws DatatypeConfigurationException {
        XMLGregorianCalendar published = atomEntry.getPublished();

        // We'll let it be as old as 2 hours ago.
        GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        currentCalendar.add(Calendar.HOUR_OF_DAY, -2);
        XMLGregorianCalendar twoHoursAgo = DatatypeFactory.newInstance().newXMLGregorianCalendar(currentCalendar);

        return published.compare(twoHoursAgo) == DatatypeConstants.GREATER;
    }

    @Override
    public boolean isValidForSubscription(YTAtomFeed atomFeed, Subscription subscription) {
        return atomFeed.getSelfLink().getHref().equals(subscription.getTopicUrl());
    }
}
