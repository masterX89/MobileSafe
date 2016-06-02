package com.hrbeu.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.utils.MD5Utils;

/**
 * 主页面
 *
 * @author Hankai Xia
 */

public class HomeActivity extends Activity {

	private GridView gvHome;
	private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

	private int[] mPics = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps, R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan, R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings};

	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		gvHome = (GridView) findViewById(R.id.gv_home);
		gvHome.setAdapter(new HomeAdapter());
		// 设置一级目录的监听
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						// 手机防盗
						showPasswordDialog();
						break;
					case 1:
						// 通讯卫士
						startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
						break;
					case 2:
						// 软件管理
						startActivity(new Intent(HomeActivity.this, AppManagerActivity.class));
						break;
					case 3:
						// 进程管理
						startActivity(new Intent(HomeActivity.this, TaskManagerActivity.class));
						break;
					case 7:
						//高级工具
						startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
						break;
					case 8:
						// 设置中心
						startActivity(new Intent(HomeActivity.this, SettingActivity.class));
						break;

					default:
						break;
				}
			}
		});
	}

	/**
	 * 显示密码弹窗
	 */
	protected void showPasswordDialog() {
		// 判断是否设置密码
		String savedPassword = mPref.getString("password", null);
		if (!TextUtils.isEmpty(savedPassword)) {
			// 输入密码弹窗
			showPasswordInputDialog();
		} else {
			// 如果没有设置过，弹出设置密码的弹窗
			showPasswordSetDialog();
		}
	}

	/**
	 * 输入密码弹窗
	 */
	private void showPasswordInputDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_input_password, null);
		// 将自定义的布局文件设置给dialog
		// dialog.setView(view);
		// 设置边距为0，以适应2.X版本
		dialog.setView(view, 0, 0, 0, 0);

		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				if (!TextUtils.isEmpty(password)) {
					String savedPassword = mPref.getString("password", null);

					if (MD5Utils.encode(password).equals(savedPassword)) {
						Toast.makeText(HomeActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
						// 隐藏dialog
						dialog.dismiss();
						// 跳转到手机防盗页面
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏dialog
				dialog.dismiss();
			}
		});

		// 显示dialog
		dialog.show();

	}

	/**
	 * 设置密码弹窗
	 */
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_set_password, null);
		// 将自定义的布局文件设置给dialog
		// dialog.setView(view);
		// 设置边距为0，以适应2.X版本
		dialog.setView(view, 0, 0, 0, 0);

		final EditText etPassword = (EditText) view.findViewById(R.id.et_password);
		final EditText etConfirm = (EditText) view.findViewById(R.id.et_confirm);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etConfirm.getText().toString();
				// password != null && !password.equals("")
				if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirm)) {
					if (password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
						// 将密码保存起来
						mPref.edit().putString("password", MD5Utils.encode(password)).commit();

						dialog.dismiss();

						// 跳转到手机防盗页面
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					} else {
						Toast.makeText(HomeActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "输入框内容不能为空！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏dialog
				dialog.dismiss();
			}
		});

		// 显示dialog
		dialog.show();

	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);
			return view;
		}

	}
}
