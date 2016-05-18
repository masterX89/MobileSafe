package com.hrbeu.mobilesafe.db.dao;

import android.database.Cursor;
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
		String address = "未知号码";
		// 数据库对象
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		// 电话号码正则表达式匹配
		if (number.matches("^1[3-8]\\d{9}$")) {
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
		} else if (number.matches("^\\d+$")) {// 匹配数字
			switch (number.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器";
				break;
			case 5:
				address = "客服电话";
				break;
			case 7:
			case 8:
				address = "本地号码";
				break;
			default:
				if (number.startsWith("0") && number.length() > 10) {// 长途电话
					// 区号有4位3位两种
					// 先查询4位区号
					Cursor cursor = database.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {
						cursor.close();
						// 查询3位区号
						cursor = database.rawQuery(
								"select location from data2 where area=?",
								new String[] { number.substring(1, 3) });
						if (cursor.moveToNext()) {
							address = cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}
		}
		database.close();
		return address;
	}
}
