package com.net128.app.wechatin.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class WeChatUtil {
    private final static Logger logger = LoggerFactory.getLogger(WeChatUtil.class);

    public static final String TOKEN = "wolfcode";
    public static final String APPID = "wx59687be81dd3d388";
    public static final String APPSECRET = "d4624c36b6795d1d99dcf0547af5443d";

    public static final String WECHAT_API = "https://api.weixin.qq.com";
    public static final String CREATE_MENU_URL = WECHAT_API +"/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public static final String GET_ACCESSTOKEN_URL = WECHAT_API + "/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static final String DELETE_MENU_URL = WECHAT_API + "/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    public static final String GET_TICKET_URL = WECHAT_API + "/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static final String GET_WEB_ACCESSTOKEN_URL = WECHAT_API + "/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static final String GET_USERINFO_URL = WECHAT_API + "/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static String accessToken;
    public static long expiresTime;

    public static JSONObject getWebAccessToken(String code){
        String result = HttpUtil.get(GET_WEB_ACCESSTOKEN_URL.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code));
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    public static String getAccessToken(){
        if(accessToken==null||new Date().getTime()>expiresTime){
            String result = HttpUtil.get(GET_ACCESSTOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET));
            JSONObject json = JSONObject.parseObject(result);
            accessToken = json.getString("access_token");
            Long expires_in = json.getLong("expires_in");
            expiresTime = new Date().getTime()+((expires_in-60)*1000);
        }

        return accessToken;
    }

    public static void createMenu(String menuJson){
        String result = HttpUtil.post(CREATE_MENU_URL.replace("ACCESS_TOKEN",getAccessToken()), menuJson);
        logger.debug(result);
    }

    public static void deleteMenu(){
        String result = HttpUtil.get(DELETE_MENU_URL.replace("ACCESS_TOKEN", getAccessToken()));
        logger.debug(result);
    }

    public static final String SEND_TEMPLATE_URL = WECHAT_API + "/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public static void sendTemplate(String data){
        String result = HttpUtil.post(SEND_TEMPLATE_URL.replace("ACCESS_TOKEN", getAccessToken()),data);
        logger.debug(result);
    }

    public static JSONObject getUserInfo(String accessToken,String openId){
        String result = HttpUtil.get(GET_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID",openId));
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }

    public static String getTicket(){
        String result = HttpUtil.get(GET_TICKET_URL.replace("ACCESS_TOKEN",getAccessToken()));
        JSONObject json = JSONObject.parseObject(result);
        logger.debug(result);
        return json.getString("ticket");
    }

    public static String getSignature(String jsapi_ticket,Long timestamp,String noncestr,String url ){
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