package com.net128.app.wechatin.domain.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    @See: https://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/sending-messages/customer-service-msgs#sending-messages_customer-service-msgs
*/
public class CustomMessage extends InternalMessage {
    public String touser;
    public MessageType msgtype;
    public Text text;
    public MediaMessage image;
    public MediaMessage voice;
    public MediaMessage video;
    public MusicMessage music;
    public NewsMessage news;
    public enum MessageType {
        text, image, voice, video, music, news
    }

    public CustomMessage() {}
    public CustomMessage(String text) {
        init(MessageType.text, null);
        this.text.content=text;
    }
    public CustomMessage(MessageType messageType, String mediaId) {
        init(messageType, mediaId);
    }
    public CustomMessage(MessageType messageType, String mediaId, String thumbMediaId) {
        init(messageType, mediaId);
        video.thumb_media_id=thumbMediaId;
    }
    public CustomMessage(String title, String description, String musicUrl,
            String hqMusicUrl, String thumbMediaId) {
        init(MessageType.music, null);
        music.thumb_media_id=thumbMediaId;
        music.title=title;
        music.description=description;
        music.hqmusicurl=hqMusicUrl;
    }
    public CustomMessage(Article ... articles) {
        this(new ArrayList<>(Arrays.asList(articles)));
    }
    public CustomMessage(List<Article> articles) {
        init(MessageType.news, null);
        news.articles=articles;
    }
    public CustomMessage to(String to) {
        touser=to;
        return this;
    }

    public static class Article {
        public String title;
        public String description;
        public String url;
        public String picurl;

        public Article(){}
        public Article(String title, String description, String url, String picurl) {
            this.title = title;
            this.description = description;
            this.url = url;
            this.picurl = picurl;
        }
    }

    private void init(MessageType messageType, String mediaId) {
        msgtype=messageType;
        switch (messageType) {
            case text: text=new Text(); break;
            case image: image=new MediaMessage(mediaId); break;
            case voice: voice=new MediaMessage(mediaId); break;
            case video: video=new MediaMessage(mediaId); break;
            case music: music=new MusicMessage(); break;
            case news: news=new NewsMessage(); break;
        }
    }

    public static class Text {
        public String content;
    }

    private static class MediaMessage {
        public String media_id;
        public String thumb_media_id;
        public MediaMessage() {}
        public MediaMessage(String mediaId) {
            this.media_id=mediaId;
        }
    }

    private static class MusicMessage {
        public String title;
        public String description;
        public String musicurl;
        public String hqmusicurl;
        public String thumb_media_id;
    }

    private static class NewsMessage {
        public List<Article> articles=new ArrayList<>();
    }
}
