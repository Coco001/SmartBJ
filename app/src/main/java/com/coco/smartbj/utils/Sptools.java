package com.coco.smartbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 初始化加载界面的工具类
 */

public class Sptools {
    /**
     * 设置保存的标识
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, String key, boolean value){
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIGFILE, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();//提交保存键值对
    }

    public static boolean getBoolean(Context context, String key, boolean value) {
        return context.getSharedPreferences(Constant.CONFIGFILE, Context.MODE_PRIVATE).getBoolean(key, value);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIGFILE, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();//提交保存键值对
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(Constant.CONFIGFILE, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }
}
