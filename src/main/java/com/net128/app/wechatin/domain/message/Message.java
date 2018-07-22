package com.net128.app.wechatin.domain.message;

import com.net128.app.wechatin.domain.XmlObject;

public class Message extends InternalMessage {
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
