package com.hrbeu.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.hrbeu.mobilesafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hankai Xia on 2016-05-23.
 */
public class BlackNumberDao {

	private final BlackNumberOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberOpenHelper(context);
	}

	/**
	 * @param number 黑名单号码
	 * @param mode   拦截模式
	 */
	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("mode", mode);
		long rowID = db.insert("blacknumber", null, contentValues);
		if (rowID == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 通过电话号码删除
	 *
	 * @param number 电话号码
	 */
	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 通过电话号码修改拦截模式
	 *
	 * @param number 电话号码
	 */
	public boolean changeNumberMode(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("mode", mode);
		int rowNumber = db.update("blacknumber", contentValues, "number=?", new String[]{number});
		if (rowNumber == 0) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 返回黑名单的拦截方式
	 *
	 * @return
	 */
	public String findNumber(String number) {
		String mode = "";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
		if (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * 查询所有的黑名单
	 *
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(3000);//模拟从网络读取数据
		return blackNumberInfos;
	}

	/**
	 * 分页加载数据
	 *
	 * @param pageNumber 当前的页数
	 * @param pageSize   每一页数据的条数
	 * @return
	 */
	public List<BlackNumberInfo> findPar(int pageNumber, int pageSize) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(pageSize), String.valueOf(pageSize * pageNumber)});
		ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}

	/**
	 * 分批加载数据
	 * @param startIndex 开始的位置
	 * @param maxCount   每一页展示的最大的条目
	 * @return
	 */
	public List<BlackNumberInfo> findPar2(int startIndex, int maxCount) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(maxCount), String.valueOf(startIndex)});
		ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
		while (cursor.moveToNext()) {
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			blackNumberInfos.add(blackNumberInfo);
		}
		cursor.close();
		db.close();
		return blackNumberInfos;
	}

	/**
	 * 获取总的记录条数
	 *
	 * @return 所有的记录条数
	 */
	public int getTotalNumber() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
	}
}
