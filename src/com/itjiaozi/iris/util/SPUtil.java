package com.itjiaozi.iris.util;

import com.itjiaozi.iris.TheApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    public static void put(String key, boolean value) {
        TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static void put(String key, int value) {
        TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static void put(String key, long value) {
        TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public static void put(String key, String value) {
        TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).getString(key, defValue);
    }
}
