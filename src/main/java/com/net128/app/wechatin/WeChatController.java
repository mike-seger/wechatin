package com.net128.app.wechatin;

import com.net128.app.wechatin.config.WeChatProperties;
import com.net128.app.wechatin.domain.InMessage;
import com.net128.app.wechatin.domain.OutMessage;
import com.net128.app.wechatin.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

@Controller
public class WeChatController {
    private final static Logger logger = LoggerFactory.getLogger(WeChatController.class);

    public final static String wechat="util";

    @Autowired
    private WeChatProperties wcp;

    @GetMapping(wechat)
    @ResponseBody
    public String validate(String signature,String timestamp,String nonce,String echostr){
        String[] arr = {wcp.getToken(),timestamp,nonce};
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String temp : arr) {
            sb.append(temp);
        }
        String mySignature = HashUtil.SHA1(sb.toString());
        if(mySignature.equals(signature)){
            logger.debug("Signature correct");
            return echostr;
        }
        logger.error("Signature incorrect");
        return null;
    }

    @PostMapping(value=wechat, produces=MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Object handleMessage(@RequestBody InMessage inMessage) {
        logger.info("{} -> {}", inMessage.FromUserName, inMessage.Content);
        OutMessage outMessage = new OutMessage();
        outMessage.FromUserName=inMessage.ToUserName;
        outMessage.ToUserName=inMessage.FromUserName;
        outMessage.CreateTime=new Date().getTime();
        String msgType = inMessage.MsgType;
        String content = inMessage.Content;
        String outContent = new StringBuilder(content).reverse().toString();
        outMessage.Content=outContent;
        outMessage.MsgType="text";
        return outMessage;
    }
}