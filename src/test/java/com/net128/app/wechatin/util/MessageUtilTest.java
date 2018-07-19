package com.net128.app.wechatin.util;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MessageUtilTest {
    private final static String appId = "wxf1569d816b304d28";
    private final static String encodingAESKey = "WO3Gcs2X5lcNtZagAcaRRjNaAwEmoekP1P2aOKR4W3D";
    private final static String token = "weixin";

    private final static String message=
        "<xml><ToUserName><![CDATA[gh_3db049ae940a]]></ToUserName>\n" +
        "<FromUserName><![CDATA[ozKDGv3BQsXV2WDNDynsLnueAujU]]></FromUserName>\n" +
        "<CreateTime>1502784210</CreateTime>\n" +
        "<MsgType><![CDATA[text]]></MsgType>\n" +
        "<Content><![CDATA[Hello World!]]></Content>\n" +
        "<MsgId>6454409035319957426</MsgId>\n" +
        "</xml>";

    private final static String randomString = "gN2CpuwYRA3Wk7vH";

    private final static String encMessage =
        "DG6DTgn54EnzHiXpu5C+SYYE7JhrXiQsa0LvUk4Rgip9C9lZ8egIdPq1C3v0XdkZGPl/yEt97dp0NwDiq5CqhbkW7G0EXv/t2C4AllDdjuPnguaFUQQcp1tHjeMJlOiLdGJKqYrYrIuklDkhD/ZMpK1XsGQZrXJo6yRU601+nDRXayo0rZGJ9Ue/m7CfI/m3bc+Rg5KPakvXBSIZrt3RZ0vciGsR4EG5F9acZF4HqEU6A9z8UCdz6IYhr0K42d2Ua0QTEZZK03JMnU0sQm8xZ9vpBJF3GK1DR2ZwFQq83HMu8RTZjIvHWVQlzx5slFaU2dsb5Rm/v9mx+N7m8zZSJAwvesadtifEp++bIr5IUBwJVdbMoUKKEcJHoANCCubH0Eyqq25utj7JZoUIaJID2Su7mx4G5406GCPvbvu5rg9vHKT1d+X7pYFIIBviMdt6WyfA9CHWT82nzR3OfcG2gQ==";

    private MessageUtil messageUtil = new MessageUtil("abcd",  encodingAESKey, appId);

    @Test
    public void testEncrypt() {
        String theEncText = messageUtil.encrypt(randomString, message);
        assertEquals(encMessage, theEncText);
    }

    @Test
    public void testDecrypt() {
        String theText = messageUtil.decrypt(encMessage);
        assertEquals(message, theText);
    }

    @Test
    public void testEncryptMessage() {
        String theEncMessage = messageUtil.encryptMessage(message, System.currentTimeMillis()+"", new Random().nextInt()+"");
        System.out.println(theEncMessage);
    }
}
