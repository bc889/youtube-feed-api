package com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Data;


@Data
@XmlRootElement(name = "feed", namespace="http://www.w3.org/2005/Atom")
@XmlAccessorType(XmlAccessType.FIELD)
public class YTAtomFeed {

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private String title;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private XMLGregorianCalendar updated;

    @XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
    private Link selfLink;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private YTAtomEntry entry;
}
