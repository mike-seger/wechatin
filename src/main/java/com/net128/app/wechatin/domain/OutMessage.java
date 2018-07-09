package com.net128.app.wechatin.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JacksonXmlRootElement(localName="xml")
public class OutMessage implements WeChatType {
    @JacksonXmlCData
    public String ToUserName;
    @JacksonXmlCData
    public String FromUserName;
    public Long CreateTime;
    @JacksonXmlCData
    public String MsgType;
    @JacksonXmlCData
    public String Content;
    public int FuncFlag;
}
