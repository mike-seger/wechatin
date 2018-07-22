package com.net128.app.wechatin.domain.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.net128.app.wechatin.domain.WeChatType;

public class AccessToken implements WeChatType {
    public String access_token;
    public int expires_in;
    @JsonIgnore
    public long expires;
}
