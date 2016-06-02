package com.hrbeu.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.utils.UIUtils;
import com.hrbeu.mobilesafe.view.SettingItemView;

/**
 * 第二个设置向导页
 * 
 * @author Hankai Xia
 * 
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		sivSim = (SettingItemView) findViewById(R.id.siv_sim);

		String sim = mPref.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			sivSim.setChecked(true);
		} else {
			sivSim.setChecked(false);
		}

		sivSim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivSim.isChecked()) {
					sivSim.setChecked(false);
					// 删除已绑定的SIM卡
					mPref.edit().remove("sim").commit();
				} else {
					sivSim.setChecked(true);
					// 保存SIM卡的信息
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					// 获取SIM卡序列号
					String simSerialNumber = tm.getSimSerialNumber();
					System.out.println("SIM卡序列号" + simSerialNumber);
					// 将SIM卡序列号保存在SharedPreferences中
					mPref.edit().putString("sim", simSerialNumber).commit();
				}
			}
		});
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}

	@Override
	public void showNextPage() {
		// 如果SIM卡没有绑定，就不允许进入下一个界面
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			UIUtils.showToast(this, "必须绑定SIM卡");
			return;
		}
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		// 两个界面切换的动画
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);

	}
}
