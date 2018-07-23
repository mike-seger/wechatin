package com.net128.app.wechatin.controller;

import com.net128.app.wechatin.domain.message.CustomMessage;
import com.net128.app.wechatin.domain.UserInfo;
import com.net128.app.wechatin.domain.message.Message;
import com.net128.app.wechatin.service.WeChatService;
import com.net128.app.wechatin.util.LimitedList;
import com.net128.app.wechatin.util.MessageUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@ResponseBody
public class WeChatAccessController {
    private final static Logger logger = LoggerFactory.getLogger(WeChatAccessController.class);

    private final static String wechat = "wechat";
    private final static String history = "history";
    private final static String send = "sendMessage";
    private final static String users = "users";

    @Autowired
    private WeChatService weChatService;

    private List<Message> receivedMessages = new LimitedList<>(100);

    private long messageCount;

    @GetMapping(history)
    public Object history(@RequestParam int n) {
        n = Math.min(n, receivedMessages.size());
        return receivedMessages.subList(receivedMessages.size() - n, receivedMessages.size());
    }

    @GetMapping(wechat)
    public String validate(@RequestParam String signature,
           @RequestParam String timestamp, @RequestParam String nonce,
           @RequestParam String echostr) {
        if (weChatService.validateSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }

    @PostMapping(send)
    public void sendMessage(@RequestBody CustomMessage message,
            @ApiParam(hidden = true) @RequestHeader HttpHeaders headers,
            @ApiParam(hidden = true) HttpServletRequest request) {
        weChatService.sendMessage(message);
    }

    @GetMapping(users)
    public List<UserInfo> listUsers() {
        return weChatService.getFollowingUsers();
    }

    @PostMapping(value = wechat, produces = MediaType.APPLICATION_XML_VALUE)
    public Object acceptMessage(@RequestBody Message message,
            @ApiParam(hidden = true) @RequestHeader HttpHeaders headers,
            @ApiParam(hidden = true) @RequestHeader(required = false) String aesKey,
            @ApiParam(hidden = true) @RequestHeader(required = false) String token,
            @ApiParam(hidden = true) @RequestHeader(required = false) String appId,
            @ApiParam(hidden = true) HttpServletRequest request) {
        boolean isEncrypted=false;
        MessageUtil mu=null;
        if(message.Encrypt!=null) {
            isEncrypted=true;
            mu=new MessageUtil(token, aesKey, appId);
            message=new Message().fromXml(mu.decryptMessage(message));
        }
        message.id = messageCount++;
        message.sent = LocalDateTime.now();
        message.headers = headers.entrySet();
        message.remoteAddress = request.getRemoteAddr();
        receivedMessages.add(message);
        logger.info("{} -> {}", message.FromUserName, message.Content);
        Message outMessage = new Message();
        outMessage.FromUserName = message.ToUserName;
        outMessage.ToUserName = message.FromUserName;
        outMessage.CreateTime = new Date().getTime();
        String content = message.Content + "";
        String outContent = new StringBuilder(content).reverse().toString();
        outMessage.Content = outContent;
        outMessage.MsgType = "text";
        if(isEncrypted) {
            outMessage=mu.encryptMessage(outMessage);
        }
        return outMessage;
    }
}