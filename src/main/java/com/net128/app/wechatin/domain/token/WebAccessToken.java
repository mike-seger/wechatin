package com.net128.app.wechatin.domain.token;

import com.net128.app.wechatin.domain.WeChatType;

public class WebAccessToken implements WeChatType {
    public String access_token;
    public int expires_in;
    public String refresh_token;
    public String openid;
    public String scope;
    public String unionid;
}
