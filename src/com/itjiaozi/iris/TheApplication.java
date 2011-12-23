package com.itjiaozi.iris;

import com.itjiaozi.iris.db.EADbHelper;
import com.itjiaozi.iris.util.SPUtil;

import android.app.Application;

public class TheApplication extends Application {
    private static final String TAG = TheApplication.class.getSimpleName();
    private static TheApplication INSTANCE;

    public static TheApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(appUncaughtExceptionHandler);
        INSTANCE = this;

        init();

        EADbHelper.getInstance();
    }

    private void init() {
        // 设置讯飞应用ID
        SPUtil.put(Constant.SP_KEY_XUNFEI_APP_ID, "4ead074b");
    }

    java.lang.Thread.UncaughtExceptionHandler appUncaughtExceptionHandler = new java.lang.Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread t, Throwable ex) {
            try {
                java.io.StringWriter w = new java.io.StringWriter();
                java.io.PrintWriter pw = new java.io.PrintWriter(w);
                new Throwable(ex).printStackTrace();
                ex.printStackTrace(pw);
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.io.FileWriter f = new java.io.FileWriter(new java.io.File("sdcard/bug.txt"), true);
                f.write("\r\n" + df.format(new java.util.Date(System.currentTimeMillis())) + "\r\n");
                f.write(w.toString().replace("\n", "\r\n"));
                f.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    };
}
