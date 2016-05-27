package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.service.AddressService;
import com.hrbeu.mobilesafe.service.CallSafeService;
import com.hrbeu.mobilesafe.utils.ServiceStatusUtils;
import com.hrbeu.mobilesafe.view.SettingClickView;
import com.hrbeu.mobilesafe.view.SettingItemView;

/**
 * 设置中心
 *
 * @author Hankai Xia
 */
public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;// 设置升级
	private SettingItemView sivCallSafe;// 设置黑名单
	private SettingItemView sivAddress;// 设置归属地
	private SettingClickView scvAddressStyle;// 修改归属地提示风格
	private SettingClickView scvAddressLocation;// 修改归属地提示位置
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 更新SharedPreferences
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initUpdateView();
		initAddressView();
		initAddressStyle();
		initAddressLocation();
		initBlackNumberView();
	}

	private void initBlackNumberView() {
		sivCallSafe = (SettingItemView) findViewById(R.id.siv_callsafe);
		// 判断CallSafeService是否开启
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.hrbeu.mobilesafe.service.CallSafeService");
		if (serviceRunning) {
			sivCallSafe.setChecked(true);
		} else {
			sivCallSafe.setChecked(false);
		}
		sivCallSafe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sivCallSafe.isChecked()) {
					sivCallSafe.setChecked(false);
					stopService(new Intent(SettingActivity.this, CallSafeService.class));// 停止黑名单服务
				} else {
					sivCallSafe.setChecked(true);
					startService(new Intent(SettingActivity.this, CallSafeService.class));// 开启黑名单服务
				}
			}
		});
	}

	/**
	 * 初始化自动更新
	 */
	private void initUpdateView() {
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		// sivUpdate.setTitle("自动更新设置");
		boolean autoUpdate = mPref.getBoolean("auto_update", true);
		if (autoUpdate) {
			// sivUpdate.setDesc("自动更新已开启");
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("自动更新已关闭");
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

	/**
	 * 初始化归属地设置
	 */
	private void initAddressView() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// 判断AddressService是否开启
		boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.hrbeu.mobilesafe.service.AddressService");
		if (serviceRunning) {
			sivAddress.setChecked(true);
		} else {
			sivAddress.setChecked(false);
		}
		sivAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sivAddress.isChecked()) {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this, AddressService.class));// 停止归属地服务
				} else {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this, AddressService.class));// 开启归属地服务
				}
			}
		});
	}

	final String[] items = new String[]{"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

	/**
	 * 修改归属地提示框的风格
	 */
	private void initAddressStyle() {
		scvAddressStyle = (SettingClickView) findViewById(R.id.scv_address_style);
		scvAddressStyle.setTitle("归属地提示框风格");
		int style = mPref.getInt("address_style", 0);// 读取保存的style
		scvAddressStyle.setDesc(items[style]);

		scvAddressStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDialog();
			}
		});
	}

	/**
	 * 弹出选择风格的单选框
	 */
	protected void showSingleChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("归属地提示框风格");
		int style = mPref.getInt("address_style", 0);// 读取保存的style
		builder.setSingleChoiceItems(items, style, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPref.edit().putInt("address_style", which).commit();// 保存用户选择的风格
				dialog.dismiss();// dialog消失
				scvAddressStyle.setDesc(items[which]);// 更新组合控件的描述信息
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 修改归属地显示位置
	 */
	private void initAddressLocation() {
		scvAddressLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvAddressLocation.setTitle("归属地提示框显示位置");
		scvAddressLocation.setDesc("设置归属地提示框的显示位置");
		scvAddressLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingActivity.this, DragViewActivity.class));
			}
		});
	}
}