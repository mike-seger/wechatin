package com.net128.app.wechatin;

import com.net128.app.wechatin.config.WeChatProperties;
import com.net128.app.wechatin.domain.InMessage;
import com.net128.app.wechatin.domain.OutMessage;
import com.net128.app.wechatin.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/util")
public class UtilController {
    private final static Logger logger = LoggerFactory.getLogger(UtilController.class);

    public final static String encrypt ="encrypt";

    @Autowired
    private WeChatProperties wcp;

    @PostMapping(value= encrypt, produces=MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Object encrypt(@RequestBody InMessage inMessage) {
        logger.info("{} -> {}", inMessage.FromUserName, inMessage.Content);
        return inMessage;
    }
}