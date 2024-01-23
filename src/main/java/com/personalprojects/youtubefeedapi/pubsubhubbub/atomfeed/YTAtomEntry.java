package com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed;
import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


@Data
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class YTAtomEntry {

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private String id;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private String title;

    @XmlElement(namespace = "http://www.youtube.com/xml/schemas/2015")
    private String videoId;

    @XmlElement(namespace = "http://www.youtube.com/xml/schemas/2015")
    private String channelId;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private Author author;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private Link link;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private XMLGregorianCalendar published;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private XMLGregorianCalendar updated;
}
