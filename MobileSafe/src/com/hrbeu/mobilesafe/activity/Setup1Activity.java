package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hrbeu.mobilesafe.R;

/**
 * 第一个设置向导页
 * 
 * @author Hankai Xia
 * 
 */
public class Setup1Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setup1);
	}

	/**
	 * 跳转到下一页
	 * 
	 * @param view
	 */
	public void next(View view) {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}
}
