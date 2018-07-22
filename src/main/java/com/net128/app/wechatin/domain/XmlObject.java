package com.net128.app.wechatin.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="xml")
public interface XmlObject {
    XmlMapper xmlMapper = new XmlMapper();;
    default String toXml() {
        try {
            return xmlMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping object to XML", e);
        }
    }
}
