package com.itjiaozi.iris.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.itjiaozi.iris.db.easyandroid.EABaseModel;
import com.itjiaozi.iris.db.easyandroid.EADBField;
import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.Pinyin;
import com.itjiaozi.iris.util.TheObservable;

public class TbAppCache extends EABaseModel {
    public static TheObservable onChanged = new TheObservable();

    public static final String TB_NAME = TbAppCache.class.getSimpleName();

    private static final String TAG = TbAppCache.class.getSimpleName();

    public static class Columns {
        public static final String _ID = "_ID";
        public static final String BeUsedCount = "BeUsedCount";
        public static final String Name = "Name";
        public static final String PinYin = "PinYin";
        public static final String PackageName = "PackageName";
        public static final String VersionName = "VersionName";
        public static final String VersionCode = "VersionCode";
        public static final String IconPath = "IconPath";
    }

    @EADBField(mode = EADBFieldMode.Key)
    public long _ID;
    @EADBField
    public int BeUsedCount;
    @EADBField
    public String Name;
    @EADBField
    public String PinYin;
    @EADBField
    public String PackageName;
    @EADBField
    public String VersionName;
    @EADBField
    public int VersionCode;
    @EADBField
    public String IconPath;

    public static long insertOrUpdate(String name, String packageName, String versionName, int versionCode, String iconPath) {
        long id = -1;
        ContentValues values = new ContentValues();
        values.put(Columns.BeUsedCount, 0);
        values.put(Columns.Name, name);
        values.put(Columns.PinYin, Pinyin.getPingYin(name));
        values.put(Columns.VersionName, versionName);
        values.put(Columns.VersionCode, versionCode);
        values.put(Columns.IconPath, iconPath);

        String sql = String.format("SELECT * FROM %s WHERE %s=?", TB_NAME, Columns.PackageName);
        long count = getCount(sql, new String[] { packageName });
        if (0 == count) {
            values.put(Columns.PackageName, packageName);
            id = EADbHelper.getInstance().insert(TB_NAME, null, values);
        } else {
            id = EADbHelper.getInstance().update(TB_NAME, values, Columns.PackageName + "=?", new String[] { packageName });
        }
        if (id > -1) {
            AppLog.d(TAG, "向库中添加程序成功: name" + name + ", package:" + packageName);
        } else {
            AppLog.e(TAG, "向库中添加程序失败: name" + name + ", package:" + packageName);
        }
        return id;
    }

    public static long getCount(String sql, String[] selectionArgs) {
        long count = 0;
        Cursor c = null;
        try {
            c = EADbHelper.getInstance().rawQuery(sql, selectionArgs);
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
        } finally {
            if (null != c) {
                c.close();
            }
        }
        return count;
    }

    public static List<TbAppCache> queryLikeAppByName(String name) {
        String appNamePinyin = Pinyin.getPingYin(name);
        AppLog.d(TAG, String.format("查找应用:%s, \t拼音:%s", name, appNamePinyin));
        List<TbAppCache> result = new ArrayList<TbAppCache>();
        Cursor c = null;
        try {
            c = EADbHelper.getInstance().query(TB_NAME, null, Columns.PinYin + "=?", new String[] { appNamePinyin }, null, null, Columns.BeUsedCount + " DESC");
            for (c.moveToFirst(); c.isLast(); c.moveToNext()) {
                TbAppCache tmp = new TbAppCache();
                tmp._ID = c.getLong(c.getColumnIndex(Columns._ID));
                tmp.BeUsedCount = c.getInt(c.getColumnIndex(Columns.BeUsedCount));
                tmp.IconPath = c.getString(c.getColumnIndex(Columns.IconPath));
                tmp.Name = c.getString(c.getColumnIndex(Columns.Name));
                tmp.PackageName = c.getString(c.getColumnIndex(Columns.PackageName));
                tmp.PinYin = c.getString(c.getColumnIndex(Columns.PinYin));
                result.add(tmp);
            }
        } finally {
            if (null != c) {
                c.close();
            }
        }
        if (result.size() < 1) {
            AppLog.w(TAG, String.format("未找到应用:%s, \t拼音:%s", name, appNamePinyin));
        }
        return result;
    }

    public static int deletePackage(String packageName) {
        int len = EADbHelper.getInstance().delete(TB_NAME, Columns.PackageName + "=?", new String[] { packageName });
        if (0 == len) {
            AppLog.e(TAG, "删除成失败, 从数据库中清除包:" + packageName);
        } else {
            AppLog.d(TAG, "删除成功, 从数据库中清除包:" + packageName);
        }
        return len;
    }

    public static List<String> queryAppNames() {
        List<String> list = new ArrayList<String>();
        Cursor c = null;
        try {
            c = EADbHelper.getInstance().query(TB_NAME, null, null, null, null, null, null);
            for (c.moveToFirst(); !c.isLast(); c.moveToNext()) {
                String appName = c.getString(c.getColumnIndex(Columns.Name));
                list.add(appName);
            }
        } finally {
            if (null != c) {
                c.close();
            }
        }
        return list;
    }
}
