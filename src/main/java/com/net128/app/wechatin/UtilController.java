package com.net128.app.wechatin;

import com.net128.app.wechatin.config.WeChatProperties;
import com.net128.app.wechatin.domain.InMessage;
import com.net128.app.wechatin.domain.OutMessage;
import com.net128.app.wechatin.util.HashUtil;
import com.net128.app.wechatin.util.MessageUtil;
import com.net128.app.wechatin.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
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
    public String encrypt(@RequestBody InMessage inMessage,
                          @RequestParam("token") String token,
                          @RequestParam("appId") String appId,
                          @RequestParam("aesKey") String aesKey) {
        MessageUtil messageUtil = new MessageUtil(token, aesKey, appId);
        logger.info("{} -> {}", inMessage.FromUserName, inMessage.Content);
        return messageUtil.encryptMessage(XmlUtil.toXml(inMessage), System.currentTimeMillis()+"", random.nextInt()+"");
    }
}