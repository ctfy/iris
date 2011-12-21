package com.itjiaozi.iris.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.itjiaozi.iris.db.easyandroid.EABaseModel;
import com.itjiaozi.iris.db.easyandroid.EADBField;
import com.itjiaozi.iris.db.easyandroid.EADBField.EADBFieldMode;
import com.itjiaozi.iris.util.TheObservable;

public class TbHistory extends EABaseModel {
    public static TheObservable onChanged = new TheObservable();

    public static final String TB_NAME = TbHistory.class.getSimpleName();

    public static class Columns {
        public static final String _id = "_id";
        public static final String Content = "Content";
        public static final String Type = "Type";
        public static final String Time = "Time";
        public static final String TAG1 = "TAG1";
        public static final String TAG2 = "TAG2";
    }

    @EADBField(mode = EADBFieldMode.Key)
    public long _id;
    @EADBField
    public String Content;
    @EADBField
    public int Type;
    @EADBField
    public long Time;
    @EADBField
    public String TAG1;
    @EADBField
    public String TAG2;

    public static long insert(String content, int type) {
        ContentValues values = new ContentValues();
        values.put(Columns.Content, content);
        values.put(Columns.Type, type);
        values.put(Columns.Time, System.currentTimeMillis());
        long result = EADbHelper.getInstance().insert(TB_NAME, null, values);
        onChanged.notifyObservers(result);
        return result;
    }

    public static Cursor query(String string) {
        return EADbHelper.getInstance().query(TB_NAME, null, null, null, null, null, "");
    }

    public static Cursor query() {
        return EADbHelper.getInstance().query(TB_NAME, null, null, null, null, null, null);
    }
}
