package com.itjiaozi.iris.about;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.db.EADbHelper;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.SPUtil;
import com.itjiaozi.iris.util.TheObservable;

public class TheApps {

    public static TheObservable onChanged = new TheObservable();

    protected static final String TAG = TheApps.class.getSimpleName();

    public TheApps() {
        syncApps();
    }

    public static void syncApps() {
        AsyncTask<Object, String, Object> task = new AsyncTask<Object, String, Object>() {
            public final static String HAS_UPDATE_APP_INFO = "HAS_UPDATE_APP_INFO ";
            boolean hasUpdateAppInfo = false;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                hasUpdateAppInfo = TheApplication.getInstance().getSharedPreferences(TheApps.class.getName(), Context.MODE_PRIVATE).contains(HAS_UPDATE_APP_INFO);
            }

            @Override
            protected Object doInBackground(Object... params) {
                AppLog.d(TAG, hasUpdateAppInfo ? "程序列表已经同步,跳过" : "程序列表未同步过");
                if (!hasUpdateAppInfo) {
                    List<PackageInfo> packages = TheApplication.getInstance().getPackageManager().getInstalledPackages(0);
                    for (int i = 0, len = packages.size(); i < len; i++) {
                        AppLog.d(TAG, String.format("同步应用信息进度: %s/%s", 1 + i, len));
                        PackageInfo packageInfo = packages.get(i);
                        updateAppInfo(packageInfo);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                TheApplication.getInstance().getSharedPreferences(TheApps.class.getName(), Context.MODE_PRIVATE).edit().putBoolean(HAS_UPDATE_APP_INFO, true).commit();
            }
        };
        task.execute(0);

    }

    public static void updateAppInfo(String packageName) {
        try {
            PackageInfo packageInfo = TheApplication.getInstance().getPackageManager().getPackageInfo(packageName, 0);
            updateAppInfo(packageInfo);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void updateAppInfo(PackageInfo packageInfo) {
        TbAppCache appCache = new TbAppCache();
        String name = packageInfo.applicationInfo.loadLabel(TheApplication.getInstance().getPackageManager()).toString();
        String packageName = packageInfo.packageName;
        String versionName = packageInfo.versionName;
        int versionCode = packageInfo.versionCode;
        // TODO 存储图标路径
        Drawable drawable = packageInfo.applicationInfo.loadIcon(TheApplication.getInstance().getPackageManager());

        TbAppCache.insertOrUpdate(name, packageName, versionName, versionCode, "");

        autoAddVersion();

        onChanged.notifyObservers(packageName);
    }

    public static List<TbAppCache> query(String str) {
        return TbAppCache.queryLikeAppByName(str);
    }

    public static void deletePackage(String packageName) {
        TbAppCache.deletePackage(packageName);

        autoAddVersion();

        onChanged.notifyObservers(packageName);
    }

    public static List<String> getAllAppName() {
        return TbAppCache.queryAppNames();
    }

    public static void autoAddVersion() {
        int version = getVersion();
        version += 1;
        AppLog.d(TAG, "应用程序数据缓存版本已经升级至：" + version);
        SPUtil.put(TheApps.class.getName() + "_version", version);
    }

    public static int getVersion() {
        int version = SPUtil.getInt(TheApps.class.getName() + "_version", 0);
        AppLog.d(TAG, "当前应用程序缓存数据版本：" + version);
        return version;
    }
}
