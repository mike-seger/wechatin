package com.net128.app.wechatin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccessToken implements WeChatType {
    public String access_token;
    public int expires_in;
    @JsonIgnore
    public long expires;
}
