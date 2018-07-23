package com.net128.app.wechatin.domain;

import com.net128.app.wechatin.domain.message.CustomMessage;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomMessageTest {
    private static final Logger logger = LoggerFactory.getLogger(CustomMessageTest.class);
    @Test
    public void testText() {
        logger.info(new CustomMessage("Hello World").to("OPENID").toJsonPretty());
    }
    @Test
    public void testArticle() {
        logger.info(new CustomMessage(
            new CustomMessage.Article("title", "description", "url", "picurl"),
            new CustomMessage.Article("title", "description", "url", "picurl")
        ).to("OPENID").toJsonPretty());
    }
}
