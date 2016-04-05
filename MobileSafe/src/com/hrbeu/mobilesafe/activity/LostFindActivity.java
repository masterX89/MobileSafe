package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrbeu.mobilesafe.R;

/**
 * 手机防盗页面
 * 
 * @author Hankai Xia
 * 
 */
public class LostFindActivity extends Activity {

	private SharedPreferences mPref;
	private TextView tvSafePhone;
	private ImageView ivProtect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPref = getSharedPreferences("config", MODE_PRIVATE);
		// 判断是否进入过设置向导，默认没有进入过
		boolean configed = mPref.getBoolean("configed", false);
		if (configed) {
			setContentView(R.layout.activity_lost_find);
			// 根据sp更新安全号码
			tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
			String phone = mPref.getString("safe_phone", "");
			tvSafePhone.setText(phone);
			// 根据sp更新锁
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPref.getBoolean("protect", false);
			if (protect) {
				ivProtect.setImageResource(R.drawable.lock);
			} else {
				ivProtect.setImageResource(R.drawable.unlock);

			}
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
