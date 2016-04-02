package com.hrbeu.mobilesafe.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 
 * @author Hankai Xia
 * 
 */
public class SplashActivity extends Activity {

	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_UPDATE_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;

	private TextView tvVersion;
	private TextView tvProgress;// 下载进度

	// 服务器返回信息
	private String mVersionName; // 版本名
	private int mVersionCode; // 版本号
	private String mDesc; // 版本描述
	private String mDownLoadUrl; // 下载地址

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDailog();
				break;
			case CODE_UPDATE_ERROR:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析错误",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号:" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);// 默认隐藏
		checkVersion();
	}

	/**
	 * 获取版本名称
	 * 
	 * @return 版本名称
	 */
	private String getVersionName() {

		PackageManager packageManager = getPackageManager();

		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			// 输出结果到LogCat
			// System.out.println("版本号:" + versionCode + ";版本名:" + versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取本地版本号
	 * 
	 * @return 版本号
	 */
	private int getVersionCode() {

		PackageManager packageManager = getPackageManager();

		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		// 启动子线程异步加载
		new Thread() {

			@Override
			public void run() {
				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					// 本机地址：localhost，模拟器访问本机地址：10.0.2.2
					URL url = new URL("http://10.0.2.2:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					// 设置请求方法：GET
					conn.setRequestMethod("GET");
					// 设置连接超时
					conn.setConnectTimeout(5000);
					// 设置响应超时
					conn.setReadTimeout(5000);
					// 连接服务器
					conn.connect();
					// 获取响应码
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						// 输出结果到LogCat
						// System.out.println("网络结果：" + result);
						// 解析json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDesc = jo.getString("description");
						mDownLoadUrl = jo.getString("downloadUrl");
						// 输出json解析结果
						System.out.println("版本名：" + mVersionName + ",版本号："
								+ mVersionCode + ",版本描述：" + mDesc + ",下载地址："
								+ mDownLoadUrl);

						// 判斷是否有更新
						if (mVersionCode > getVersionCode()) {
							// 服务器的VersionCode大于本地的VersionCode
							// 说明有更新，弹出升级对话框
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// URL错误
					msg.what = CODE_UPDATE_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络错误
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// json解析异常
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;// 访问网络花费的时间
					if (timeUsed < 2000) {
						// 保证splash展示2秒
						try {
							Thread.sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
					if (conn != null) {
						conn.disconnect();
					}
				}
			}

		}.start();

	}

	/**
	 * 升级对话框
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本" + mVersionName);
		builder.setMessage(mDesc);
		// 设置对话框点击返回键不关闭，用户体验过低
		// builder.setCancelable(false);
		// 设置确定按钮的点击事件
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("立即更新");
				download();
			}
		});
		// 设置取消按钮的点击事件
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("以后再说");
				enterHome();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		// 显示对话框
		builder.show();
	}

	/**
	 * 下载apk文件
	 */
	protected void download() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			tvProgress.setVisibility(View.VISIBLE);// 显示进度
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			// 使用XUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mDownLoadUrl, target, new RequestCallBack<File>() {

				// 下载文件的进度
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("下载进度" + current + "/" + total);
					tvProgress.setText("下载进度" + current * 100 / total + "%");
				}

				// 下载成功
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("下载成功");
					// 跳转到系统的下载页面
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					// Android 6.0编译失败，应该使用Xutils3
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					// startActivity(intent);
					// 用戶取消安裝会返回结果，回调方法onActivityResult
					startActivityForResult(intent, 0);
				}

				// 下载失败
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "下载失败",
							Toast.LENGTH_LONG).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "没有找到sd卡", Toast.LENGTH_LONG)
					.show();
		}
	}

	// 用戶取消安裝会返回结果，回调方法onActivityResult
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 进入主界面
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
