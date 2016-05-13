package com.hrbeu.mobilesafe.db.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询工具
 * 
 * @author Hankai Xia
 * 
 */
public class AddressDao {
	// 该路径必须是data/data目录下
	private static final String PATH = "data/data/com.hrbeu.mobilesafe/files/address.db";

	public static String getAddress(String number) {
		// 数据库对象
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		return null;
	}
}
