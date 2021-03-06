package com.hrbeu.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Toast显示工具
 *
 * @author Hankai Xia
 */
public class UIUtils {
	public static void showToast(final Activity context, final String msg) {
		if ("main".equals(Thread.currentThread().getName())) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		} else {
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
