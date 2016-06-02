package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.service.KillProcessService;
import com.hrbeu.mobilesafe.utils.SharedPreferencesUtils;
import com.hrbeu.mobilesafe.utils.SystemInfoUtils;

public class TaskManagerSettingActivity extends Activity {

	private SharedPreferences sp;
	private CheckBox cb_status_kill_process;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	private void initUI() {
		setContentView(R.layout.activity_task_manager_setting);
		CheckBox cb_status = (CheckBox) findViewById(R.id.cb_status);

		//设置是否选中
		cb_status.setChecked(SharedPreferencesUtils.getBoolean(TaskManagerSettingActivity.this, "is_show_system", false));

		cb_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferencesUtils.saveBoolean(TaskManagerSettingActivity.this, "is_show_system", isChecked);
			}
		});

		//定时清理进程

		cb_status_kill_process = (CheckBox) findViewById(R.id.cb_status_kill_process);
		final Intent intent = new Intent(this, KillProcessService.class);

		cb_status_kill_process.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					startService(intent);
				} else {
					stopService(intent);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (SystemInfoUtils.isServiceRunning(TaskManagerSettingActivity.this, "com.hrbeu.mobilesafe.service.KillProcessService")) {
			cb_status_kill_process.setChecked(true);
		} else {
			cb_status_kill_process.setChecked(false);
		}
	}
}
