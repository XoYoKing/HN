package com.example.admin.hn.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 一.本地数据的储存
 * 1.需要包含android.content.SharedPreferences包：import android.content.SharedPreferences;
 * 2. 保存数据到本地：
 */
public class SharedPreferencesSession extends Session {

    private static final String DEFAULT_SESSION_FILE = "config";

    protected static class Value {

        protected String value;

        protected long expireTime;

        public Value(String value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public Value(String value) {
            this(value, 0);
        }

        public String toString() {
            return expireTime + ":" + value;
        }

        public static Value toValue(String valueStr) {
            if (valueStr == null) {
                return null;
            }

            int pos = valueStr.indexOf(':');
            if (pos == -1) {
                return  new Value(valueStr);
            }

            return new Value(valueStr.substring(pos + 1), Long.parseLong(valueStr.substring(0, pos)));
        }

    }

    protected SharedPreferences sp;

    public SharedPreferencesSession(Context context) {
        this(context, DEFAULT_SESSION_FILE);
    }

    public SharedPreferencesSession(Context context, String filename) {
        this.sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
    }

    @Override
    public void setString(String key, String value, long expireSeconds) {
        // TODO Auto-generated method stub
        if (expireSeconds <= 0) {
            this.sp.edit().putString(key, new Value(value, 0).toString()).commit();
        } else {
            this.sp.edit().putString(key, new Value(value, new Date().getTime() / 1000 + expireSeconds).toString()).commit();
        }
    }

    @Override
    public void set(Map<String, String> vals, long expireSeconds) {
        // TODO Auto-generated method stub
        Editor editor = this.sp.edit();
        for (Iterator i = vals.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = vals.get(key);
            if (expireSeconds <= 0) {
                editor.putString(key, new Value(value, 0).toString());
            } else {
                editor.putString(key, new Value(value, new Date().getTime() / 1000 + expireSeconds).toString());
            }
        }
        editor.commit();
    }

    @Override
    public String getString(String key,String defaultValue) {
        // TODO Auto-generated method stub
        String string = this.sp.getString(key, defaultValue);
        Value val = Value.toValue(string);
        if (val != null) {
            if (val.expireTime <= 0 || val.expireTime > new Date().getTime() / 1000) {
                return val.value;
            }
        }
        return null;
    }

    public void putInt(String key, int value) {
        this.sp.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defaultValue) {
        return this.sp.getInt(key, defaultValue);
    }

    @Override
    public void putFloat(String key, float value) {
        this.sp.edit().putFloat(key, value);
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return this.sp.getFloat(key, defaultValue);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        this.sp.edit().putBoolean(key, value).commit();
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return this.sp.getBoolean(key, defaultValue);
    }


}
