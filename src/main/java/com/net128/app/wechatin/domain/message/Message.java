package com.net128.app.wechatin.domain.message;

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

    //Encryped version - may be standalone or combined in mixed-mode
    public String Encrypt;
    public String MsgSignature;
    public String TimeStamp;
    public String Nonce;
}
