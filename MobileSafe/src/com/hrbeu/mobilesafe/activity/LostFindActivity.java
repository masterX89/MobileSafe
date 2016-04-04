package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.hrbeu.mobilesafe.R;

/**
 * 手机防盗页面
 * 
 * @author Hankai Xia
 * 
 */
public class LostFindActivity extends Activity {
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPref = getSharedPreferences("config", MODE_PRIVATE);
		// 判断是否进入过设置向导，默认没有进入过
		boolean configed = mPref.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
		} else {
			// 跳转设置向导页面
			startActivity(new Intent(LostFindActivity.this,
					Setup1Activity.class));
			finish();
		}
	}

	/**
	 * 重新进入设置向导
	 * 
	 * @param view
	 */
	public void reEnter(View view) {
		startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
		finish();
	}
}
