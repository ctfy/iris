package com.itjiaozi.iris.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

public class AppLog implements Runnable {

    public static final boolean DEBUG = true;
    public static final boolean APP_TAG = true;

    public static final String LOG_FILE_PATH = "/mnt/sdcard/debug_iris.txt";
    static java.io.FileWriter f;
    public static final String TAG = "lenovo_lv";

    public static void v(String TAG, String msg) {
        Log.v(TAG, msg);
        if (APP_TAG) {
            Log.v("LvAppLog", TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void e(String TAG, String msg, Throwable t) {
        Log.e(TAG, msg, t);
        if (APP_TAG) {
            Log.e("LvAppLog", TAG + ":" + msg, t);
        }
        write2file(msg);
    }

    public static void w(String TAG, String msg) {
        Log.w(TAG, msg);
        if (APP_TAG) {
            Log.w("LvAppLog", TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
        if (APP_TAG) {
            Log.d("LvAppLog", TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void e(String TAG, String msg) {
        Log.e(TAG, msg);
        if (APP_TAG) {
            Log.e("LvAppLog", TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void i(String TAG, String msg) {
        Log.i(TAG, msg);
        if (APP_TAG) {
            Log.i("LvAppLog", TAG + ":" + msg);
        }
        write2file(msg);
    }

    private static Queue<String> msgQueue = new LinkedList<String>();
    private static Thread writeThread;

    private static void write2file(String msg) {
        if (null == writeThread) {
            writeThread = new Thread(new AppLog());
            writeThread.start();
        }
        msgQueue.offer(msg);
    }

    @Override
    public void run() {
        int i = 1;
        while (true) {
            if (msgQueue.size() > 0) {
                String msg = msgQueue.poll();
                try {
                    if (null == f) {
                        f = new java.io.FileWriter(LOG_FILE_PATH, true);
                    }
                    f.write(msg + "\r\n");
                    if (i++ % 30 == 0) {
                        f.flush();
                    }
                } catch (IOException e) {
                    try {
                        f.close();
                    } catch (IOException e1) {
                    }
                    f = null;
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

}
