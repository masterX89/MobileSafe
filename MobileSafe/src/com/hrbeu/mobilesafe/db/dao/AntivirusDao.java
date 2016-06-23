package com.hrbeu.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Hankai Xia on 2016-06-23.
 */
public class AntivirusDao {

	private static final String PATH = "data/data/com.hrbeu.mobilesafe/files/antivirus.db";

	/**
	 * 检查当前md5是否在数据库中
	 *
	 * @param md5
	 * @return
	 */
	public static String checkFileVirus(String md5) {
		String desc = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		//查询当前传过来的md5是否在病毒数据库里面
		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?", new String[]{md5});
		//判断当前的游标是否可以移动
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		return desc;
	}
}
