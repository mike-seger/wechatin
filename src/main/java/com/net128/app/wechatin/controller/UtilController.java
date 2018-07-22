package com.net128.app.wechatin.controller;

import com.net128.app.wechatin.config.WeChatProperties;
import com.net128.app.wechatin.domain.message.Message;
import com.net128.app.wechatin.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping("/util")
public class UtilController {
    private final static Logger logger = LoggerFactory.getLogger(UtilController.class);

    public final static String encrypt ="encrypt";

    @Autowired
    private WeChatProperties wcp;

    private Random random=new Random();

    @PostMapping(value= encrypt, produces=MediaType.TEXT_XML_VALUE)
    @ResponseBody
    public String encrypt(@RequestBody Message message,
            @RequestParam("token") String token,
            @RequestParam("appId") String appId,
            @RequestParam("aesKey") String aesKey) {
        MessageUtil messageUtil = new MessageUtil(token, aesKey, appId);
        logger.info("{} -> {}", message.FromUserName, message.Content);
        return messageUtil.encryptMessage(message.toXml(), System.currentTimeMillis()+"", random.nextInt()+"");
    }
}