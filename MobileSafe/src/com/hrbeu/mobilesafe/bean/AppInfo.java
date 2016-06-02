package com.hrbeu.mobilesafe.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Hankai Xia on 2016-05-27.
 */
public class AppInfo {
	/**
	 * 图片的icon
	 */
	private Drawable icon;
	/**
	 * 程序的名字
	 */
	private String apkName;
	/**
	 * 程序的大小
	 */
	private long apkSize;
	/**
	 * 表示是否是用户app
	 */
	private boolean isUserApp;
	/**
	 * 是否放置在rom中
	 */
	private boolean isRom;
	/**
	 * 包名
	 */
	private String apkPackageName;

	@Override
	public String toString() {
		return "AppInfo{" +
				"isRom=" + isRom +
				", apkPackageName='" + apkPackageName + '\'' +
				", isUserApp=" + isUserApp +
				", apkSize=" + apkSize +
				", apkName='" + apkName + '\'' +
				'}';
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public long getApkSize() {
		return apkSize;
	}

	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean userApp) {
		isUserApp = userApp;
	}

	public boolean isRom() {
		return isRom;
	}

	public void setRom(boolean rom) {
		isRom = rom;
	}

	public String getApkPackageName() {
		return apkPackageName;
	}

	public void setApkPackageName(String apkPackageName) {
		this.apkPackageName = apkPackageName;
	}
}
