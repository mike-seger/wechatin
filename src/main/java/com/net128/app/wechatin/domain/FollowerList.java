package com.net128.app.wechatin.domain;

public class FollowerList implements WeChatType {
    public int total;
    public int count;
    public Data data;
    public static class Data {
        public String [] openid;
    }
    public String next_openid;
}
