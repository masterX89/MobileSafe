package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hrbeu.mobilesafe.R;

/**
 * 第三个设置向导页
 * 
 * @author Hankai Xia
 * 
 */
public class Setup3Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup3);
	}

	/**
	 * 跳转到下一页
	 * 
	 * @param view
	 */
	public void next(View view) {
		startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	/**
	 * 跳转到上一页
	 * 
	 * @param view
	 */
	public void previous(View view) {
		startActivity(new Intent(Setup3Activity.this, Setup2Activity.class));
		finish();
	}
}
