package com.lee.xqq.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final int version = 1;
	private static SQLiteHelper instance;
	private static String[] sqls;

	private SQLiteHelper(Context context, String name) {
		this(context, name, null, version);
	}

	private SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 初始要执行的SQL语句数组
	 * @param sqls
	 */
	public static void init(String[] sqls) {
		SQLiteHelper.sqls = sqls;
	}

	public static SQLiteHelper getInstance(Context context, String name) {
		if (instance == null)
			instance = new SQLiteHelper(context, name);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (int i = 0; sqls != null && i < sqls.length; i++)
			db.execSQL(sqls[i]);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
