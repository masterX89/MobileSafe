package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.db.dao.AddressDao;

/**
 * 归属地查询页面
 *
 * @author Hankai Xia
 */
public class AddressActivity extends Activity {
	private EditText etNumber;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_address);
		etNumber = (EditText) findViewById(R.id.et_number);
		tvResult = (TextView) findViewById(R.id.tv_result);
		// 监听EditText的变化
		etNumber.addTextChangedListener(new TextWatcher() {
			/**
			 * 发生变化时的回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}

			/**
			 * 变化前的回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {

			}

			/**
			 * 文字变化结束后的回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}
		});
	}

	/**
	 * 查询号码
	 *
	 * @param view
	 */
	public void query(View view) {
		String number = etNumber.getText().toString().trim();
		if (!TextUtils.isEmpty(number)) {
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			etNumber.startAnimation(shake);
			vibrate();
		}
	}

	/**
	 * 手机震动，需要权限android.permission.VIBRATE
	 */
	private void vibrate() {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// vibrator.vibrate(2000);
		vibrator.vibrate(new long[]{500, 500, 500, 500}, -1);// 等待0.5s，震动0.5s，等待0.5s，震动0.5s，-1不循环，0从第0位循环
		// vibrator.cancel();
	}
}
