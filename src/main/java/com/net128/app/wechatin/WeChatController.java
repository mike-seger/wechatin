package com.net128.app.wechatin;

import com.net128.app.wechatin.domain.InMessage;
import com.net128.app.wechatin.domain.OutMessage;
import com.net128.app.wechatin.util.HashUtil;
import com.net128.app.wechatin.util.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;

@Controller
public class WeChatController {
    private final static Logger logger = LoggerFactory.getLogger(WeChatController.class);
    @RequestMapping(value="weChat",method = RequestMethod.GET)
    @ResponseBody
    public String validate(String signature,String timestamp,String nonce,String echostr){
        String[] arr = {WeChatUtil.TOKEN,timestamp,nonce};
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

    @RequestMapping(value="weChat",method = RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Object handleMessage(@RequestBody InMessage inMessage) {
        OutMessage outMessage = new OutMessage();
        outMessage.FromUserName=inMessage.ToUserName;
        outMessage.ToUserName=inMessage.FromUserName;
        outMessage.CreateTime=new Date().getTime();
        String msgType = inMessage.MsgType;
        String content = inMessage.Content;
        String outContent = "You said: "+content;
        outMessage.Content=outContent;
        outMessage.MsgType="text";
        return outMessage;
    }
}