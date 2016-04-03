package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 * 
 * @author Hankai Xia
 * 
 */
public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;// 设置升级
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 更新SharedPreferences
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDisc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDisc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断当前的勾选状态
				if (sivUpdate.isChecked()) {
					// 设置不勾选
					sivUpdate.setChecked(false);
					// sivUpdate.setDisc("自动更新已关闭");
					// 更新SharedPreferences
					mPref.edit().putBoolean("auto_update", false).commit();
				} else {
					// 设置勾选
					sivUpdate.setChecked(true);
					// sivUpdate.setDisc("自动更新已开启");
					// 更新SharedPreferences
					mPref.edit().putBoolean("auto_update", true).commit();
				}
			}
		});
	}
}