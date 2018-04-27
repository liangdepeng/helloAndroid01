package com.example.zhengjun.helloandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class Storage {

    private SharedPreferences storage = null;

    private SharedPreferences.Editor editor = null;

    public Storage(Context context) {
        this.storage = context.getSharedPreferences("reactor", context.MODE_PRIVATE);
        this.editor  = this.storage.edit();
    }

    public String get(String key) {
        return this.storage.getString(key, "");
    }

    public String get(String key, String defaultString) {
        return this.storage.getString(key, defaultString);
    }

    public void set(String key, String value) {
        this.editor.putString(key, value);
        this.editor.apply();
    }

    public void clear() {
        this.editor.clear();
        this.editor.apply();
    }

    public JSONObject getJson(String key) throws JSONException {
        String value = this.storage.getString(key, "{}");
        return new JSONObject(value);
    }

    public void setJson(String key, JSONObject value) {
        this.editor.putString(key, value.toString());
        this.editor.apply();
    }
}
