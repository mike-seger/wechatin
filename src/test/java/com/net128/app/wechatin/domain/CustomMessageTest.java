package com.net128.app.wechatin.domain;

import com.net128.app.wechatin.domain.message.CustomMessage;
import org.junit.Test;

public class CustomMessageTest {
    @Test
    public void testText() {
        System.out.println(new CustomMessage("Hello World").to("OPENID").toJsonPretty());
    }
    @Test
    public void testArticle() {
        System.out.println(new CustomMessage(
            new CustomMessage.Article("title", "description", "url", "picurl"),
            new CustomMessage.Article("title", "description", "url", "picurl")
        ).to("OPENID").toJsonPretty());
    }
}
