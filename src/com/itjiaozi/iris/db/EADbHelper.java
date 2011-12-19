package com.itjiaozi.iris.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.itjiaozi.iris.TheApplication;
import com.itjiaozi.iris.db.easyandroid.BaseEASQLiteOpenHelper;

public class EADbHelper extends BaseEASQLiteOpenHelper {
    private static EADbHelper mInstance;

    public static EADbHelper getInstance() {
        if (null == mInstance) {
            mInstance = new EADbHelper(TheApplication.getInstance());
        }
        return mInstance;
    }
    
    public static final String DB_NAME = "ea2.db";
    public static final int DB_VERSION = 2;
    private EADbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
        db.execSQL(new TbHistory().createTableSql());
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        db.execSQL(new TbHistory().dropTableSql());
        onCreate(db);
    }
}
