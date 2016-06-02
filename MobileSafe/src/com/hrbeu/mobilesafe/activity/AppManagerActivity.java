package com.hrbeu.mobilesafe.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.bean.AppInfo;
import com.hrbeu.mobilesafe.engine.AppInfos;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity implements View.OnClickListener {

	@ViewInject(R.id.list_view)
	private ListView listView;
	@ViewInject(R.id.tv_rom)
	private TextView tvRom;
	@ViewInject(R.id.tv_sd)
	private TextView tvSd;
	@ViewInject(R.id.tv_app)
	private TextView tvApp;
	private List<AppInfo> appInfos;
	private ArrayList<AppInfo> userAppInfos;
	private ArrayList<AppInfo> systemAppInfos;
	private PopupWindow popupWindow;
	private AppInfo clickAppInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_uninstall:
				Intent uninstallLocalIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + clickAppInfo.getApkPackageName()));
				startActivity(uninstallLocalIntent);
				popupWindowDismiss();
				break;
			case R.id.ll_run:
				Intent runLocalIntent = this.getPackageManager().getLaunchIntentForPackage(clickAppInfo.getApkPackageName());
				this.startActivity(runLocalIntent);
				popupWindowDismiss();
				break;
			case R.id.ll_share:
				Intent shareLocalIntent = new Intent("android.intent.action.SEND");
				shareLocalIntent.setType("text/plain");
				shareLocalIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
				shareLocalIntent.putExtra("android.intent.extra.TEXT", "Hi！推荐您使用软件：" + clickAppInfo.getApkName() + "下载地址:" + "https://play.google.com/store/apps/details?id=" + clickAppInfo.getApkPackageName());
				this.startActivity(Intent.createChooser(shareLocalIntent, "分享"));
				popupWindowDismiss();
				break;
			case R.id.ll_detail:
				Intent detail_intent = new Intent();
				detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
				detail_intent.setData(Uri.parse("package:" + clickAppInfo.getApkPackageName()));
				startActivity(detail_intent);
				break;

		}


	}

	private class AppManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userAppInfos.size() + systemAppInfos.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return null;
			} else if (position == userAppInfos.size() + 1) {
				return null;
			}
			AppInfo appInfo;
			if (position < userAppInfos.size() + 1) {
				appInfo = userAppInfos.get(position - 1);
			} else {
				appInfo = systemAppInfos.get(position - userAppInfos.size() - 2);
			}
			return appInfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//position等于0表示应用程序
			if (position == 0) {
				TextView tvUserApp = new TextView(AppManagerActivity.this);
				tvUserApp.setTextColor(Color.WHITE);
				tvUserApp.setBackgroundColor(Color.GRAY);
				tvUserApp.setText("用户程序(" + userAppInfos.size() + ")");
				return tvUserApp;
			} else if (position == userAppInfos.size() + 1) {
				TextView tvSystemApp = new TextView(AppManagerActivity.this);
				tvSystemApp.setTextColor(Color.WHITE);
				tvSystemApp.setBackgroundColor(Color.GRAY);
				tvSystemApp.setText("系统程序(" + systemAppInfos.size() + ")");
				return tvSystemApp;
			}
			AppInfo appInfo;
			if (position < userAppInfos.size() + 1) {
				appInfo = userAppInfos.get(position - 1);
			} else {
				appInfo = systemAppInfos.get(position - userAppInfos.size() - 2);
			}
			View view = null;
			ViewHolder holder;
			if (convertView != null && convertView instanceof LinearLayout) {
				view = convertView;
				holder = (ViewHolder) convertView.getTag();
			} else {
				view = View.inflate(AppManagerActivity.this, R.layout.item_app_manager, null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tvName = (TextView) view.findViewById(R.id.tv_name);
				holder.tvLocation = (TextView) view.findViewById(R.id.tv_location);
				holder.tvApkSize = (TextView) view.findViewById(R.id.tv_apk_size);
				view.setTag(holder);
			}

			holder.ivIcon.setBackground(appInfo.getIcon());
			holder.tvName.setText(appInfo.getApkName());
			if (appInfo.isRom()) {
				holder.tvLocation.setText("手机内存");
			} else {
				holder.tvLocation.setText("SD卡");
			}
			holder.tvApkSize.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApkSize()));
			return view;
		}
	}

	static class ViewHolder {
		ImageView ivIcon;
		TextView tvName;
		TextView tvLocation;
		TextView tvApkSize;

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			AppManagerAdapter adapter = new AppManagerAdapter();
			listView.setAdapter(adapter);
		}
	};

	private void initData() {
		new Thread() {
			@Override
			public void run() {
				//super.run();
				//获取已安装的应用程序
				appInfos = AppInfos.getAppInfos(AppManagerActivity.this);
				//appInfos拆成用户程序集合和系统程序集合
				//用户程序集合
				userAppInfos = new ArrayList<AppInfo>();
				//系统程序集合
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp()) {
						userAppInfos.add(appInfo);
					} else {
						systemAppInfos.add(appInfo);
					}
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private void initUI() {
		setContentView(R.layout.activity_app_manager);
		//		ListView listView = (ListView) findViewById(R.id.list_view);
		ViewUtils.inject(this);
		//获取到rom内存运行的剩余空间
		long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
		//获取到SD卡的剩余空间
		long sdFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
		//格式化大小
		tvRom.setText("内存可用:" + Formatter.formatFileSize(this, romFreeSpace));
		tvSd.setText("SD卡可用" + Formatter.formatFileSize(this, sdFreeSpace));

		//listView的滚动监听
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			/**
			 * @param view
			 * @param firstVisibleItem 第一个可见条目的位置
			 * @param visibleItemCount 一页可以展示多少条目
			 * @param totalItemCount   总共的item的个数
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				popupWindowDismiss();
				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem > (userAppInfos.size() + 1)) {
						//系统应用程序
						tvApp.setText("系统程序(" + systemAppInfos.size() + ")");

					} else {
						//用户应用程序
						tvApp.setText("用户程序(" + userAppInfos.size() + ")");
					}

				}
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//获取当前点击的item对象
				Object obj = listView.getItemAtPosition(position);
				if (obj != null && obj instanceof AppInfo) {
					clickAppInfo = (AppInfo) obj;
					View contentView = View.inflate(AppManagerActivity.this, R.layout.item_popup, null);

					LinearLayout llUninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
					LinearLayout llRun = (LinearLayout) contentView.findViewById(R.id.ll_run);
					LinearLayout llShare = (LinearLayout) contentView.findViewById(R.id.ll_share);
					LinearLayout llDetail = (LinearLayout) contentView.findViewById(R.id.ll_detail);


					llUninstall.setOnClickListener(AppManagerActivity.this);
					llRun.setOnClickListener(AppManagerActivity.this);
					llShare.setOnClickListener(AppManagerActivity.this);
					llDetail.setOnClickListener(AppManagerActivity.this);

					popupWindowDismiss();
					popupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
					//使用popupWindow必须使用背景来使动画生效
					popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] location = new int[2];
					//获取到view展示到窗体上面的位置
					view.getLocationInWindow(location);
					popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 70, location[1]);
					ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					scaleAnimation.setDuration(150);
					contentView.startAnimation(scaleAnimation);
				}
			}
		});
	}


	private void popupWindowDismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	protected void onDestroy() {
		popupWindowDismiss();
		super.onDestroy();
	}
}
