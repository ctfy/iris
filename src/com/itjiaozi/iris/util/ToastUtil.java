package com.itjiaozi.iris.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.itjiaozi.iris.TheApplication;

public class ToastUtil {
	/** 打一个Toask, 默认时长为Toast.LENGTH_LONG */
	public static void showToast(String str) {
		showToast(str, true);
	}

	public static void showToast(final String str, final boolean shortTime) {
		Handler h = new Handler(Looper.getMainLooper());
		h.post(new Runnable() {

			@Override
			public void run() {
				if (shortTime) {
					Toast.makeText(TheApplication.getInstance(), "" + str, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(TheApplication.getInstance(), "" + str, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
