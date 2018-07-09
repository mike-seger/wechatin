package com.net128.app.wechatin.domain;

public class InMessage implements WeChatType {
    public String ToUserName;
    public String FromUserName;
    public long CreateTime;
    public String MsgType;
    public Long MsgId;

    public String Content;

    public String PicUrl;	
    public String MediaId;
    public String Event;
    public String EventKey;
}