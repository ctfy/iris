package com.itjiaozi.iris.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.itjiaozi.iris.db.easyandroid.EABaseModel;
import com.itjiaozi.iris.db.easyandroid.EADBField;
import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;
import com.itjiaozi.iris.util.TheObservable;

public class TbAppCache extends EABaseModel {
    public static TheObservable onChanged = new TheObservable();

    public static final String TB_NAME = TbAppCache.class.getSimpleName();

    public static class Columns {
        public static final String _ID = "_ID";
        public static final String BeUsedCount = "BeUsedCount";
        public static final String Name = "Name";
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
}
