package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.hrbeu.mobilesafe.R;

/**
 * 第四个设置向导页
 * 
 * @author Hankai Xia
 * 
 */
public class Setup4Activity extends Activity {

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup4);

		mPref = getSharedPreferences("config", MODE_PRIVATE);
	}

	/**
	 * 设置完成
	 * 
	 * @param view
	 */
	public void next(View view) {
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		// 更新SharedPreferences，表示已经展示过设置向导
		mPref.edit().putBoolean("configed", true).commit();
	}

	/**
	 * 跳转到上一页
	 * 
	 * @param view
	 */
	public void previous(View view) {
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}
}
