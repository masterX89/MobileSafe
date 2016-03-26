package com.hrbeu.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.hrbeu.mobilesafe.R;

public class SplashActivity extends Activity {

	private TextView tvVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾��:" + getVersionName());
	}

	/**
	 * ��ȡ�汾����
	 * 
	 * @return �汾����
	 */
	private String getVersionName() {

		PackageManager packageManager = getPackageManager();

		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * �ӷ�������ȡ�汾��Ϣ����У��
	 */
	private void checkVersion() {
		// �������߳��첽����
		new Thread() {
			@Override
			public void run() {
				try {
					// ������ַ��localhost��ģ�������ʱ�����ַ��10.0.2.2
					URL url = new URL("http://10.0.2.2:8080/update.json");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					// �������󷽷���GET
					conn.setRequestMethod("GET");
					// �������ӳ�ʱ
					conn.setConnectTimeout(5000);
					// ������Ӧ��ʱ
					conn.setReadTimeout(5000);
					// ���ӷ�����
					conn.connect();
					// ��ȡ��Ӧ��
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
					}
				} catch (MalformedURLException e) {
					// URL����
					e.printStackTrace();
				} catch (IOException e) {
					// �������
					e.printStackTrace();
				}
			}

		}.start();

	}

}
