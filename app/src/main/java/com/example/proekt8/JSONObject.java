package com.example.proekt8;

import org.json.JSONException;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class JSONObject {

    private final Map<String, Object> map;

    public JSONObject() {
        map = new HashMap<>();
    }

    public JSONObject(String source) throws JSONException {
        this(new JSONTokener(source));
    }

    public JSONObject(JSONTokener x) throws JSONException {
        this();
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }

        for (;;) {
            c = x.nextClean();
            switch (c) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    key = x.nextValue().toString();
            }

            c = x.nextClean();
            if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            map.put(key, x.nextValue());

            switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == '}') {
                        return;
                    }
                    x.back();
                    break;
                case '}':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }

    public JSONObject put(String key, Object value) throws JSONException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        if (value != null) {
            map.put(key, value);
        }
        return this;
    }

    public Object get(String key) throws JSONException {
        if (!map.containsKey(key)) {
            throw new JSONException("JSONObject[" + quote(key) + "] not found.");
        }
        return map.get(key);
    }

    public String getString(String key) throws JSONException {
        Object object = get(key);
        if (object instanceof String) {
            return (String) object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
    }

    private String quote(String string) {
        return "\\" + string + "\\";
    }

}
