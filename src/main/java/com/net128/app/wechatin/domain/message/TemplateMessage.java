package com.net128.app.wechatin.domain.message;

import com.net128.app.wechatin.domain.JsonObject;

import java.util.Map;

public class TemplateMessage implements JsonObject {
    public String touser;
    public String template_id;
    public String url;
    public String topcolor;
    public Map<String, Entry> data;

    public static class Entry {
        public String value;
        public String color;
    }
}
