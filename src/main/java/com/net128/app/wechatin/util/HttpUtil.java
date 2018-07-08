package com.net128.app.wechatin.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String get(String url) {

        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");
            conn.connect();
            try (InputStream in = conn.getInputStream()) {
                result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
            }
        } catch (Exception e) {
            logger.error("Failed to get url: {}", url, e);
        }
        return result;
    }

    public static String post(String url, String paramStr) {
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            if (StringUtils.hasText(paramStr)) {
                 try (OutputStream os = conn.getOutputStream()) {
                     os.write(paramStr.getBytes("utf-8"));
                 }
            }
            try (InputStream in = conn.getInputStream()) {
                result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
            }
        } catch (Exception e) {
            logger.error("Failed to post to url: {}, {}", url, paramStr, e);
        }
        return result;
    }

    public static void getImage(String url,OutputStream out) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestProperty("Accept", "image/jpeg");
            conn.setRequestMethod("GET");
            conn.connect();
            try (InputStream in = conn.getInputStream()) {
                try (OutputStream theOut = out) {
                    StreamUtils.copy(in, theOut);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get image: {}", url, e);
        }
    }
}