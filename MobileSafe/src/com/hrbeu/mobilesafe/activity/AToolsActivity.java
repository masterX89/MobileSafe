package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.utils.SmsUtils;
import com.hrbeu.mobilesafe.utils.UIUtils;

/**
 * 高级工具
 *
 * @author Hankai Xia
 */
public class AToolsActivity extends Activity {

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_atools);
	}

	/**
	 * 电话归属地查询
	 *
	 * @param view
	 */
	public void numberAddressQuery(View view) {
		startActivity(new Intent(this, AddressActivity.class));
	}

	/**
	 * 备份短信
	 *
	 * @param view
	 */
	public void backUpSms(View view) {
		pd = new ProgressDialog(AToolsActivity.this);
		pd.setTitle("提示");
		pd.setMessage("正在备份短信。。。");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();

		new Thread() {
			@Override
			public void run() {
				boolean result = SmsUtils.backUP(AToolsActivity.this, new SmsUtils.BackUpSms() {
					@Override
					public void befor(int count) {
						pd.setMax(count);

					}

					@Override
					public void onBackUpSms(int process) {
						pd.setProgress(process);
					}
				});
				if (result) {
					//Looper.prepare();
					//Toast.makeText(AToolsActivity.this, "短信备份成功", Toast.LENGTH_SHORT).show();
					//Looper.loop();
					UIUtils.showToast(AToolsActivity.this, "短信备份成功");
				} else {
					UIUtils.showToast(AToolsActivity.this, "短信备份失败");
					//Looper.prepare();
					//Toast.makeText(AToolsActivity.this, "短信备份失败", Toast.LENGTH_SHORT).show();
					//Looper.loop();
				}
				pd.dismiss();
			}
		}.start();
	}
}
