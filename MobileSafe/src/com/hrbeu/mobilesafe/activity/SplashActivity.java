package com.hrbeu.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.utils.StreamUtils;

/**
 * 
 * @author Hankai Xia
 * 
 */
public class SplashActivity extends Activity {

	private TextView tvVersion;
	private String mVersionName;	//版本名
	private int mVersionCode;		//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号:" + getVersionName());

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
			System.out.println("版本号:" + versionCode + ";版本名:" + versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion() {
		// 启动子线程异步加载
		new Thread() {
			


			@Override
			public void run() {
				try {
					// 本机地址：localhost，模拟器访问本机地址：10.0.2.2
					URL url = new URL("http://10.0.2.2:8080/update.json");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
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
						System.out.println("网络结果：" + result);
						// 解析json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
					}
				} catch (MalformedURLException e) {
					// URL错误
					e.printStackTrace();
				} catch (IOException e) {
					// 网络错误
					e.printStackTrace();
				} catch (JSONException e) {
					// json解析异常
					e.printStackTrace();
				}
			}

		}.start();

	}

}
