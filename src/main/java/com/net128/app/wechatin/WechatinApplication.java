package com.net128.app.wechatin;

import com.net128.app.wechatin.util.AESShortKeySupportFixer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WechatinApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		AESShortKeySupportFixer.fixKeyLength();
		SpringApplication.run(WechatinApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WechatinApplication.class);
	}
}
