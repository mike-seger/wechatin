package com.net128.app.wechatin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.net128.app.wechatin.config.WeChatProperties;
import com.net128.app.wechatin.domain.*;
import com.net128.app.wechatin.util.HashUtil;
import com.net128.app.wechatin.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class WeChatService {
    private final static Logger logger = LoggerFactory.getLogger(WeChatService.class);
    
    @Autowired
    private WeChatProperties wcp;

    private static final String WECHAT_API = "https://api.weixin.qq.com/";
    private static final String WC_API = WECHAT_API + "cgi-bin/";
    private static final String CREATE_MENU_URL = WC_API + "/menu/create?access_token=ACCESS_TOKEN";
    private static final String GET_ACCESSTOKEN_URL = WC_API + "token?grant_type=client_credential&appid=wcp.appId&secret=wcp.appSecret";
    private static final String DELETE_MENU_URL = WC_API + "menu/delete?access_token=ACCESS_TOKEN";
    private static final String GET_TICKET_URL = WC_API + "ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    private static final String GET_WEB_ACCESSTOKEN_URL = WECHAT_API + "sns/oauth2/access_token?appid=wcp.appId&secret=SECRET&code=CODE&grant_type=authorization_code";
    private static final String GET_USERS_URL = WC_API + "user/get?access_token=ACCESS_TOKEN"; // TODO id n>10000 &next_openid=NEXT_OPENID
    private static final String GET_USERINFO_URL = WC_API + "user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=en";
    private static final String GET_WEB_USERINFO_URL = WECHAT_API + "sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=en";
    private static final String SEND_TEMPLATE_URL = WC_API + "message/template/send?access_token=ACCESS_TOKEN";
    private static AccessToken accessToken;

    public WebAccessToken getWebAccessToken(String code){
        String url=GET_WEB_ACCESSTOKEN_URL.replace("wcp.appId", wcp.getAppId()).replace("SECRET", wcp.getAppSecret()).replace("CODE", code);
        return api(url, WebAccessToken.class);
    }

    public String getAccessToken(){
        if(accessToken==null || new Date().getTime()>accessToken.expires){
            String url=GET_ACCESSTOKEN_URL.replace("wcp.appId", wcp.getAppId()).replace("wcp.appSecret", wcp.getAppSecret());
            accessToken = api(url, AccessToken.class);
            accessToken.expires = new Date().getTime()+((accessToken.expires_in-60)*1000);
        }

        return accessToken.access_token;
    }

    public void createMenu(String menuJson){
        String result = HttpUtil.post(url(CREATE_MENU_URL), menuJson);
        logger.debug(result);
    }

    public void deleteMenu(){
        String result = HttpUtil.get(url(DELETE_MENU_URL));
        logger.debug(result);
    }

    public void sendTemplate(String data){
        String result = HttpUtil.post(url(SEND_TEMPLATE_URL),data);
        logger.debug(result);
    }

    public FollowerList getFollowerList() {
        return apiAt(GET_USERS_URL, FollowerList.class);
    }

    public UserInfo getUserInfo(String openId){
        return apiAt(url(GET_USERINFO_URL).replace("OPENID",openId), UserInfo.class);
    }

    public UserInfo getWebUserInfo(String openId){
        return apiAt(url(GET_WEB_USERINFO_URL).replace("OPENID",openId), UserInfo.class);
    }

    public String getTicket(){
        return apiAt(GET_TICKET_URL, JsTicket.class).ticket;
    }

    public String getSignature(String jsapi_ticket,Long timestamp,String noncestr,String url ){
        Map<String,Object> map = new TreeMap<>();
        map.put("jsapi_ticket",jsapi_ticket);
        map.put("timestamp",timestamp);
        map.put("noncestr",noncestr);
        map.put("url",url);
        StringBuilder sb = new StringBuilder();
        Set<String> set = map.keySet();
        for (String key : set) {
            sb.append(key+"="+map.get(key)).append("&");
        }
        String temp = sb.substring(0,sb.length()-1);
        String signature = HashUtil.SHA1(temp);
        return signature;
    }

    public boolean isValid() {
        return wcp.getAppId()!=null && wcp.getToken()!=null && wcp.getAppSecret()!=null;
    }

    private String url(String url) {
        return url.replace("ACCESS_TOKEN",getAccessToken());
    }

    private <T> T apiAt(String url, Class<T> clazz) {
        return api(url(url), clazz);
    }

    private <T> T api(String url, Class<T> clazz) {
        String result = HttpUtil.get(url);
        try {
            return om.readValue(result, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read object from: "+url, e);
        }
    }

    private final ObjectMapper om=new ObjectMapper();
}