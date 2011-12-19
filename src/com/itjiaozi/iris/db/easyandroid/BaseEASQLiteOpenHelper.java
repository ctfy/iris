package com.itjiaozi.iris.db.easyandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseEASQLiteOpenHelper extends SQLiteOpenHelper {
	private static final String TAG = BaseEASQLiteOpenHelper.class.getSimpleName();

	public BaseEASQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return super.getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}

	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return super.getWritableDatabase().update(table, values, whereClause, whereArgs);
	}

	public int delete(String table, String whereClause, String[] whereArgs) {
		return super.getWritableDatabase().delete(table, whereClause, whereArgs);
	}

	public long insert(String table, String nullColumnHack, ContentValues values) {
		return super.getWritableDatabase().insert(table, nullColumnHack, values);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return super.getWritableDatabase().rawQuery(sql, selectionArgs);
	}

	public void executeSQL(String sql) {
		super.getWritableDatabase().execSQL(sql);
	}
}
