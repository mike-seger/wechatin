package com.net128.app.wechatin.domain.message;

import com.net128.app.wechatin.domain.JsonObject;

import java.util.Map;
import java.util.Random;

public class TemplateMessageResult implements JsonObject {
    public int errcode=0;
    public String errmsg="ok";
    public String msgid=new Random().nextInt(2000000000)+"";
}
