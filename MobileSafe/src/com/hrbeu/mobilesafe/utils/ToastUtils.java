package com.hrbeu.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast显示工具
 * 
 * @author Hankai Xia
 * 
 */
public class ToastUtils {
	public static void showToast(Context ctx, String text) {
		Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
	}
}
