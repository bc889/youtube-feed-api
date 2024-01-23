package com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "author")
@XmlAccessorType(XmlAccessType.FIELD)
public class Author {

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private String name;

    @XmlElement(namespace="http://www.w3.org/2005/Atom")
    private String uri;
}

