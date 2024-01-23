package com.personalprojects.youtubefeedapi.pubsubhubbub.atomfeed;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

    @XmlAttribute
    private String rel;

    @XmlAttribute
    private String href;
}
