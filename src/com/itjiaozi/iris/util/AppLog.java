package com.itjiaozi.iris.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;

public class AppLog implements Runnable {

    public static final boolean DEBUG = true;
    public static final boolean ENABLE_APP_TAG = true;

    public static final String LOG_FILE_PATH = "/mnt/sdcard/debug_iris.txt";
    static java.io.FileWriter f;
    public static final String APP_TAG = AppLog.class.getSimpleName();

    public static void v(String TAG, String msg) {
        Log.v(TAG, msg);
        if (ENABLE_APP_TAG) {
            Log.v(APP_TAG, TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void e(String TAG, String msg, Throwable t) {
        Log.e(TAG, msg, t);
        if (ENABLE_APP_TAG) {
            Log.e(APP_TAG, TAG + ":" + msg, t);
        }
        write2file(msg);
    }

    public static void w(String TAG, String msg) {
        Log.w(TAG, msg);
        if (ENABLE_APP_TAG) {
            Log.w(APP_TAG, TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void d(String TAG, String msg) {
        Log.d(TAG, msg);
        if (ENABLE_APP_TAG) {
            Log.d(APP_TAG, TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void e(String TAG, String msg) {
        Log.e(TAG, msg);
        if (ENABLE_APP_TAG) {
            Log.e(APP_TAG, TAG + ":" + msg);
        }
        write2file(msg);
    }

    public static void i(String TAG, String msg) {
        Log.i(TAG, msg);
        if (ENABLE_APP_TAG) {
            Log.i(APP_TAG, TAG + ":" + msg);
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
        v(APP_TAG, msg);
    }

    public static void d(String msg) {
        d(APP_TAG, msg);
    }

    public static void e(String msg) {
        e(APP_TAG, msg);
    }

    public static void i(String msg) {
        i(APP_TAG, msg);
    }

}
