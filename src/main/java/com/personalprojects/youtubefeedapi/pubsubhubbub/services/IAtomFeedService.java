package com.personalprojects.youtubefeedapi.pubsubhubbub.services;

import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomEntry;
import com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed.YTAtomFeed;
import com.personalprojects.youtubefeedapi.subscription.Subscription;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 * Handles the conversion and interpreting of payloads received by the pubsubhubbub server.
 */
public interface IAtomFeedService {

    YTAtomFeed toAtomFeed(String payload) throws JAXBException;
    boolean isRecentUpload(YTAtomEntry atomEntry) throws DatatypeConfigurationException;

    boolean isValidForSubscription(YTAtomFeed atomFeed, Subscription subscription);
}
