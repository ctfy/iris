package com.itjiaozi.iris.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.itjiaozi.iris.db.TbAppCache.Columns;
import com.itjiaozi.iris.db.easyandroid.EABaseModel;
import com.itjiaozi.iris.db.easyandroid.EADBField;
import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;
import com.itjiaozi.iris.util.AppLog;
import com.itjiaozi.iris.util.Pinyin;
import com.itjiaozi.iris.util.TheObservable;

public class TbContactCache extends EABaseModel {
    public static TheObservable onChanged = new TheObservable();

    public static final String TB_NAME = TbContactCache.class.getSimpleName();

    private static final String TAG = TbContactCache.class.getSimpleName();

    public static class Columns {
        public static final String _id = "_id";
        public static final String BeUsedCount = "BeUsedCount";
        public static final String Name = "Name";
        public static final String PinYin = "PinYin";
        public static final String Number = "Number";
        public static final String NumberType = "NumberType";
    }

    @EADBField(mode = EADBFieldMode.Key)
    public long _id;
    @EADBField
    public int BeUsedCount;
    @EADBField
    public String Name;
    @EADBField
    public String PinYin;
    @EADBField
    public String Number;
    @EADBField
    public String NumberType;

    public static long insertOrUpdate(String name, String number, String type) {
        long id = -1;
        ContentValues values = new ContentValues();
        values.put(Columns.BeUsedCount, 0);
        values.put(Columns.Name, name);
        values.put(Columns.PinYin, Pinyin.getPingYin(name));
        values.put(Columns.Number, number);
        values.put(Columns.NumberType, type);

        String sql = String.format("SELECT * FROM %s WHERE %s=?", TB_NAME, Columns.Number);
        long count = getCount(sql, new String[] { number });
        if (0 == count) {
            values.put(Columns.Number, number);
            id = EADbHelper.getInstance().insert(TB_NAME, null, values);
        } else {
            id = EADbHelper.getInstance().update(TB_NAME, values, Columns.Number + "=?", new String[] { number });
        }
        if (id > -1) {
            AppLog.d(TAG, "向库中添加程序成功: name" + name + ", package:" + number);
        } else {
            AppLog.e(TAG, "向库中添加程序失败: name" + name + ", package:" + number);
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

    public static List<TbContactCache> queryLikeContactByName(String name) {
        String appNamePinyin = Pinyin.getPingYin(name);
        AppLog.d(TAG, String.format("查找应用:%s, \t拼音:%s", name, appNamePinyin));
        List<TbContactCache> result = new ArrayList<TbContactCache>();
        Cursor c = null;
        try {
            c = EADbHelper.getInstance().query(TB_NAME, null, Columns.PinYin + "=?", new String[] { appNamePinyin }, null, null, Columns.BeUsedCount + " DESC");
            for (c.moveToFirst(); c.isLast(); c.moveToNext()) {
                TbContactCache tmp = new TbContactCache();
                tmp._id = c.getLong(c.getColumnIndex(Columns._id));
                tmp.BeUsedCount = c.getInt(c.getColumnIndex(Columns.BeUsedCount));
                tmp.Name = c.getString(c.getColumnIndex(Columns.Name));
                tmp.PinYin = c.getString(c.getColumnIndex(Columns.PinYin));
                tmp.Number = c.getString(c.getColumnIndex(Columns.Number));
                tmp.NumberType = c.getString(c.getColumnIndex(Columns.NumberType));
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

    public static int deleteContact(String number) {
        int len = EADbHelper.getInstance().delete(TB_NAME, Columns.Number + "=?", new String[] { number });
        if (0 == len) {
            AppLog.e(TAG, "删除成失败, 号码：:" + number);
        } else {
            AppLog.d(TAG, "删除成功, 号码：" + number);
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