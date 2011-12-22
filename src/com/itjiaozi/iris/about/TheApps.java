package com.itjiaozi.iris.about;

import java.util.List;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.db.EADbHelper;
import com.itjiaozi.iris.db.TbAppCache;

public class TheApps extends BaseTheAbout {

    public TheApps() {
//        flushApps();
    }

    @Override
    public void filter(String str, String pinyin) {
        super.filter(str, pinyin);
    }

    private void flushApps() {
        List<PackageInfo> packages = TheApplication.getInstance().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            TbAppCache appCache = new TbAppCache();

            String name = packageInfo.applicationInfo.loadLabel(TheApplication.getInstance().getPackageManager()).toString();
            String packageName = packageInfo.packageName;
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            // TODO 存储图标路径
            Drawable drawable = packageInfo.applicationInfo.loadIcon(TheApplication.getInstance().getPackageManager());

            TbAppCache.insertOrUpdate(name, packageName, versionName, versionCode, "");
        }
    }

    public static List<TbAppCache> query(String str) {
        return TbAppCache.queryLikeAppByName(str);
    }
}
