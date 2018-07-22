package com.net128.app.wechatin.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.IOException;

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

    default <T> T fromXml(String xml) {
        try {
            return xmlMapper.readerForUpdating(this).readValue(xml);
        } catch (IOException e) {
            throw new RuntimeException("Error mapping XML to object", e);
        }
    }
}
