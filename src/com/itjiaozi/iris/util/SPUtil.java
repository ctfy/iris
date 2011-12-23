package com.itjiaozi.iris.util;

import com.itjiaozi.iris.TheApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
	public static void put(String key, int value) {
		SharedPreferences sp = TheApplication.getInstance().getSharedPreferences(SPUtil.class.getSimpleName(), Context.MODE_PRIVATE).edit().putInt(key, value);
	}

	public void put(String key, long value) {

	}

	public void put(String key, String value) {

	}
}
