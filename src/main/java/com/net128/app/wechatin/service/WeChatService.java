package com.net128.app.wechatin.service;

import com.alibaba.fastjson.JSONObject;
import com.net128.app.wechatin.config.WeChatProperties;
import com.net128.app.wechatin.util.HashUtil;
import com.net128.app.wechatin.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeChatService {
    private final static Logger logger = LoggerFactory.getLogger(WeChatService.class);
    
    @Autowired
    private WeChatProperties wcp;

    public static final String WECHAT_API = "https://api.weixin.qq.com";
    public static final String CREATE_MENU_URL = WECHAT_API +"/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public static final String GET_ACCESSTOKEN_URL = WECHAT_API + "/cgi-bin/token?grant_type=client_credential&appid=wcp.appId&secret=wcp.appSecret";
    public static final String DELETE_MENU_URL = WECHAT_API + "/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    public static final String GET_TICKET_URL = WECHAT_API + "/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static final String GET_WEB_ACCESSTOKEN_URL = WECHAT_API + "/sns/oauth2/access_token?appid=wcp.appId&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String GET_USERINFO_URL = WECHAT_API + "/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static final String SEND_TEMPLATE_URL = WECHAT_API + "/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    public static String accessToken;
    public static long expiresTime;

    public JSONObject getWebAccessToken(String code){
        String result = HttpUtil.get(GET_WEB_ACCESSTOKEN_URL.replace("wcp.appId", wcp.getAppId()).replace("SECRET", wcp.getAppSecret()).replace("CODE", code));
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    public String getAccessToken(){
        if(accessToken==null||new Date().getTime()>expiresTime){
            String result = HttpUtil.get(GET_ACCESSTOKEN_URL.replace("wcp.appId", wcp.getAppId()).replace("wcp.appSecret", wcp.getAppSecret()));
            JSONObject json = JSONObject.parseObject(result);
            accessToken = json.getString("access_token");
            Long expires_in = json.getLong("expires_in");
            expiresTime = new Date().getTime()+((expires_in-60)*1000);
        }

        return accessToken;
    }

    public void createMenu(String menuJson){
        String result = HttpUtil.post(CREATE_MENU_URL.replace("ACCESS_TOKEN",getAccessToken()), menuJson);
        logger.debug(result);
    }

    public void deleteMenu(){
        String result = HttpUtil.get(DELETE_MENU_URL.replace("ACCESS_TOKEN", getAccessToken()));
        logger.debug(result);
    }

    public void sendTemplate(String data){
        String result = HttpUtil.post(SEND_TEMPLATE_URL.replace("ACCESS_TOKEN", getAccessToken()),data);
        logger.debug(result);
    }

    public JSONObject getUserInfo(String accessToken,String openId){
        String result = HttpUtil.get(GET_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID",openId));
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    public String getTicket(){
        String result = HttpUtil.get(GET_TICKET_URL.replace("ACCESS_TOKEN",getAccessToken()));
        JSONObject json = JSONObject.parseObject(result);
        logger.debug(result);
        return json.getString("ticket");
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
}