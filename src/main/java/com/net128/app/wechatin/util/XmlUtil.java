package com.net128.app.wechatin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlUtil {
    private final static XmlMapper mapper = new XmlMapper();;
    public static String toXml(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error mapping object to XML", e);
        }
    }
}
