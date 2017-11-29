package cn.zinus.shipping.Config;

import android.content.Context;
import android.content.SharedPreferences;

import cn.zinus.shipping.Application;

/**
 * Developer:Spring
 * DataTime :2017/7/14 13:44
 * Main Change:SharedPreferences的工具类,提供了int,String,boolean,long,float类型的读写功能
 * AppConfig.getInstance()....
 */

public class AppConfig {
    private static AppConfig appConfig;

    private SharedPreferences preferences;

    private AppConfig() {
        preferences = Application.getInstance().getSharedPreferences("zinus", Context.MODE_PRIVATE);
    }

    public static AppConfig getInstance() {
        if (appConfig == null)
            synchronized (AppConfig.class) {
                if (appConfig == null)
                    appConfig = new AppConfig();
            }
        return appConfig;
    }

    public String getLanuageType() {
        return preferences.getString("languagetype", "zh-CN");
    }

    public void setLanuageType(String lanuageType) {
        preferences.edit().putString("languagetype", lanuageType).commit();
    }

    public void putInt(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putLong(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public void putFloat(String key, float value) {
        preferences.edit().putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }
}
