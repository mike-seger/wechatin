package com.net128.app.wechatin.util;

import com.net128.app.wechatin.domain.message.Message;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MessageUtilTest {
    private final static String appId = "wxf1569d816b304d28";
    private final static String encodingAESKey = "WO3Gcs2X5lcNtZagAcaRRjNaAwEmoekP1P2aOKR4W3D";
    private final static String token = "weixin";
    private final static String timeStamp="1532225110968";
    private final static String nonce="1249198550";

    private final static String message=
        "<xml><ToUserName>gh_3db049ae940a</ToUserName>" +
        "<FromUserName>ozKDGv3BQsXV2WDNDynsLnueAu</FromUserName>" +
        "<CreateTime>1502784210</CreateTime>" +
        "<MsgType>text</MsgType>" +
        "<Content>Hello World!</Content>" +
        "<MsgId>6454409035319957426</MsgId>" +
        "</xml>";

    private final static String randomString = "gN2CpuwYRA3Wk7vH";

    private final static String encMessage =
        "DG6DTgn54EnzHiXpu5C+SeZTaESi/GyIKod4LB2lX1oMgvQNL3UU3dnlDEJYP8Qrv9IgVTwU4V6KlAyGvmQZ8XFe2iMv12aaNB0blWeCyOzKoKHe0XTrL3yxmrWSM8nMOP5snAVKAcR8R8lnvLdf1enUgTD0UJQs4LhvdXauJJVIGRMJq+ADT3jJh+pSpkkoRO4/dGnHngtM+lzO5Wk5Z0zIheCmZ9gE8Ds49YU/WgFGL8nhxR0C07PcR2Jfwyywlwh+F2Qs+Wx+TYktYH6mIBlJktjcWvbtK0GOAwQdisRaBAks5HYEJJuPYxYEdJRzFlE5C1KHIVBR6gTDT7DRnIvGFcOOl3ELbRYqvi5YzKRWZdC660/Jt+WWYLWO+7nm";

    private final static String encMessageXml =
        "<xml><Encrypt>DG6DTgn54EnzHiXpu5C+SeZTaESi/GyIKod4LB2lX1oMgvQNL3UU3dnlDEJYP8Qrv9IgVTwU4V6KlAyGvmQZ8XFe2iMv12aaNB0blWeCyOzKoKHe0XTrL3yxmrWSM8nMOP5snAVKAcR8R8lnvLdf1enUgTD0UJQs4LhvdXauJJVIGRMJq+ADT3jJh+pSpkkoRO4/dGnHngtM+lzO5Wk5Z0zIheCmZ9gE8Ds49YU/WgFGL8nhxR0C07PcR2Jfwyywlwh+F2Qs+Wx+TYktYH6mIBlJktjcWvbtK0GOAwQdisRaBAks5HYEJJuPYxYEdJRzFlE5C1KHIVBR6gTDT7DRnIvGFcOOl3ELbRYqvi5YzKRWZdC660/Jt+WWYLWO+7nm</Encrypt><MsgSignature>06dc45fa80921c0c9084c5351b3ef0d7d597eb5e</MsgSignature><TimeStamp>1532225110968</TimeStamp><Nonce>1249198550</Nonce></xml>";

    private MessageUtil messageUtil = new MessageUtil(token,  encodingAESKey, appId);

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
        String theEncMessageXml = messageUtil.encryptMessage(randomString, new Message().fromXml(message), timeStamp, nonce).toXml();
        assertEquals(encMessageXml, theEncMessageXml);
    }

    @Test
    public void testDecryptMessage() {
        String theDecMessageXml = messageUtil.decryptMessageXml(encMessageXml);
        assertEquals(message, theDecMessageXml);
    }
}
