package com.net128.app.wechatin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InMessage {
    public String ToUserName;
    public String FromUserName;
    public Long CreateTime;
    public String MsgType;
    public Long MsgId;

    public String Content;

    public String PicUrl;	
    public String MediaId;
    public String Event;
    public String EventKey;
}