package com.net128.app.wechatin.service;

import com.net128.app.wechatin.domain.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ConditionalOnProperty(name = "wechat.appsecret")
@SpringBootTest
public class WeChatServiceTest {
    @Autowired
    private WeChatService weChatService;

    @Test
    public void testGetFollowerList() {
        if(weChatService.isValid()) {
            assertFalse(weChatService.getFollowerList().data.openid.length == 0);
        }
    }

    @Test
    public void testGetTicket() {
        if(weChatService.isValid()) {
            assertNotNull(weChatService.getTicket());
        }
    }

    @Test
    public void testUserInfo() {
        if(weChatService.isValid()) {
            String [] openIds=weChatService.getFollowerList().data.openid;
            assertFalse(openIds.length == 0);
            UserInfo userInfo=weChatService.getUserInfo(openIds[0]);
            assertEquals(openIds[0], userInfo.openid);
        }
    }
}
