package com.itjiaozi.iris.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.db.TbAppCache.Columns;
import com.itjiaozi.iris.db.easyandroid.EABaseModel;
import com.itjiaozi.iris.db.easyandroid.EADBField;
import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.Pinyin;

public class TbContactCache extends EABaseModel {
    public static class Columns {
        public static final String _id = "_id";
        public static final String BeUsedCount = "BeUsedCount";
        public static final String FullName = "FullName";
        public static final String PinYin = "PinYin";
        public static final String Number = "Number";
    }

    private static final String TB_NAME = TbContactCache.class.getSimpleName();

    private static final String TAG = TbContactCache.class.getSimpleName();

    @EADBField(mode = EADBFieldMode.Key)
    public long _id;
    @EADBField
    public int BeUsedCount;
    @EADBField
    public String FullName;
    @EADBField
    public String Pinyin;
    @EADBField
    public String Number;

    public static long insertOrUpdateContact(TbContactCache cc) {
        long id = -1;
        ContentValues values = new ContentValues();
        values.put(Columns.BeUsedCount, cc.BeUsedCount);
        values.put(Columns.FullName, cc.FullName);
        values.put(Columns.PinYin, cc.Pinyin);

        String sql = String.format("SELECT count(*) FROM %s WHERE %s=?", TB_NAME, Columns.Number);
        long count = getCount(sql, new String[] { cc.Number });
        if (0 == count) {
            values.put(Columns.Number, cc.Number);
            id = EADbHelper.getInstance().insert(TB_NAME, null, values);
        } else {
            id = EADbHelper.getInstance().update(TB_NAME, values, Columns.Number + "=?", new String[] { cc.Number });
        }
        if (id > -1) {
            AppLog.d(TAG, "更新联系人成功: " + cc);
        } else {
            AppLog.e(TAG, "更新联系人失败: " + cc);
        }
        return id;
    }

    public static int deleteAll() {
        AppLog.d(TAG, "开始清楚联系人数据！");
        int len = EADbHelper.getInstance().delete(TB_NAME, null, null);
        if (0 == len) {
            AppLog.e(TAG, "清楚联系人数据缓存表失败");
        } else {
            AppLog.d(TAG, "清楚联系人数据缓存表失败");
        }
        return len;
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

    public static List<TbContactCache> queryContacts(String fullname) {
        List<TbContactCache> list = new ArrayList<TbContactCache>();
        Cursor c = null;
        try {
            c = EADbHelper.getInstance().query(TB_NAME, null, Columns.FullName + "=?", new String[] { fullname }, null, null, null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                TbContactCache cc = new TbContactCache();
                cc._id = c.getLong(c.getColumnIndex(Columns._id));
                cc.BeUsedCount = c.getInt(c.getColumnIndex(Columns.BeUsedCount));
                cc.FullName = c.getString(c.getColumnIndex(Columns.FullName));
                cc.Number = c.getString(c.getColumnIndex(Columns.Number));
                cc.Pinyin = c.getString(c.getColumnIndex(Columns.PinYin));
                list.add(cc);
            }
        } finally {
            if (null != c) {
                c.close();
            }
        }
        return list;
    }

    public static List<String> queryContactFullNames() {
        List<String> list = new ArrayList<String>();
        Cursor c = null;
        try {
            c = EADbHelper.getInstance().query(TB_NAME, null, null, null, null, null, null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String fullName = c.getString(c.getColumnIndex(Columns.FullName));
                list.add(fullName);
            }
        } finally {
            if (null != c) {
                c.close();
            }
        }
        return list;
    }
}