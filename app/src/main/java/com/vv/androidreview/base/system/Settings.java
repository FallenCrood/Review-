
package com.vv.androidreview.base.system;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author：Laofu.
 * Mail：471996049@qq.com
 * Description：存取Settings SharePrefrence 配置
 */
public class Settings {

    public final static String FILE_CONFIG = "file_config";

    /**
     * wifi缓存过期时间 分钟为单位
     */
    public final static String CACHE_OVERTIME_WIFI = "cache_overtime_wifi";
    /**
     * 其他网络缓存过期时间 天为单位
     */
    public final static String CACHE_OVERTIME_OTHER = "cache_overtime_other";
    /**
     * 是否开启缓存不过期
     */
    public final static String CACHE_OVERTIME = "cache_overtime";

    public static SharedPreferences getPreferences(String prefName) {
        return AppContext.getInstance().getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    private static void apply(SharedPreferences.Editor editor) {
        if (AppContext.isAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void putInt(String key, int value) {
        SharedPreferences preferences = getPreferences(FILE_CONFIG);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static int getInt(String key) {
        return getPreferences(FILE_CONFIG).getInt(key, -1);
    }

    public static int getInt(String key,int defaultInt) {
        return getPreferences(FILE_CONFIG).getInt(key, defaultInt);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences preferences = getPreferences(FILE_CONFIG);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static Boolean getBoolean(String key) {
        return getPreferences(FILE_CONFIG).getBoolean(key, false);
    }

    public static Boolean getBoolean(String key,boolean defaultBoolean) {
        return getPreferences(FILE_CONFIG).getBoolean(key, defaultBoolean);
    }
}
