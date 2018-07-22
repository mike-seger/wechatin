package com.net128.app.wechatin.controller;

import com.net128.app.wechatin.domain.JsonObject;
import com.net128.app.wechatin.domain.UserInfo;
import com.net128.app.wechatin.domain.message.CustomMessage;
import com.net128.app.wechatin.domain.token.AccessToken;
import com.net128.app.wechatin.repository.SessionLogRepository;
import com.net128.app.wechatin.util.HashUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@RestController
@ResponseBody
public class WeChatMockController {
    private final static Logger logger = LoggerFactory.getLogger(WeChatMockController.class);
    private final static String testAccessTokenPrefix="TEST_ACCESS_TOKEN-";
    private final static String apiRoot="cgi-bin/";

    private final Random random=new Random(1234);

    @Autowired
    private SessionLogRepository sessionLogRepository;

    @GetMapping(apiRoot+"token")
    public AccessToken getAccessToken(
            @RequestParam String grant_type,
            @RequestParam String appid,
            @RequestParam String secret) {
        AccessToken accessToken=new AccessToken();
        accessToken.access_token = getAccessToken();
        accessToken.expires_in=7200;
        return accessToken;
    }

    @GetMapping(apiRoot+"user/info")
    public UserInfo getUserInfo(
            @RequestParam String access_token,
            @RequestParam String openId,
            @RequestParam String lang) {
        UserInfo userInfo=new UserInfo();
        userInfo.openid=openId;
        userInfo.subscribe_time =
            LocalDateTime.of(2018, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        userInfo.sex=0;
        userInfo.language="en";
        userInfo.nickname="Test "+openId;
        userInfo.subscribe=1;
        return userInfo;
    }

    @PostMapping(apiRoot+"message/custom/send")
    public void sendCustomMessage(
        @RequestParam String access_token,
        @RequestBody CustomMessage message,
        @ApiParam(hidden = true) @RequestHeader HttpHeaders headers,
        @ApiParam(hidden = true) HttpServletRequest request) throws IOException {
        message.sent = LocalDateTime.now();
        message.headers = headers.entrySet();
        message.remoteAddress = request.getRemoteAddr();
        message.tokenMD5 = HashUtil.MD5(access_token);
        try {
            sessionLogRepository.log(message);
        } catch (InterruptedException e) {
            logger.error("Failed to log: {}", message.toJson(), e);
        }
    }

    @GetMapping("logs")
    public Map<String, File> logs(@RequestParam(required = false) String id) throws IOException {
        return sessionLogRepository.getLogSessionMap();
    }

    @GetMapping("logdata")
    public FileSystemResource logData(@RequestParam(required = false) String id) throws IOException {
        return new FileSystemResource(sessionLogRepository.getCurrentLogFile(id));
    }

    private String getAccessToken() {
        byte [] accessBytes = new byte[118];
        random.nextBytes(accessBytes);
        return testAccessTokenPrefix+new String(Base64.getEncoder().encode(accessBytes))
            .replace("/", "_").replace("+", "a")
            .substring(0,158-testAccessTokenPrefix.length()-1);
    }
}
