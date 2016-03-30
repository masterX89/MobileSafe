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
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

	// ������������Ϣ
	private String mVersionName; // �汾��
	private int mVersionCode; // �汾��
	private String mDesc; // �汾����
	private String mDownLoadUrl; // ���ص�ַ

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDailog();
				break;
			case CODE_UPDATE_ERROR:
				Toast.makeText(SplashActivity.this, "url����", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "���ݽ�������",
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
		tvVersion.setText("�汾��:" + getVersionName());

		checkVersion();
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
			// ��������LogCat
			// System.out.println("�汾��:" + versionCode + ";�汾��:" + versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��ȡ���ذ汾��
	 * 
	 * @return �汾��
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
	 * �ӷ�������ȡ�汾��Ϣ����У��
	 */
	private void checkVersion() {
		final long startTime = System.currentTimeMillis();
		// �������߳��첽����
		new Thread() {

			@Override
			public void run() {
				Message msg = Message.obtain();
				HttpURLConnection conn = null;
				try {
					// ������ַ��localhost��ģ�������ʱ�����ַ��10.0.2.2
					URL url = new URL("http://10.0.2.2:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
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
						String result = StreamUtils.readFromStream(inputStream);
						// ��������LogCat
						// System.out.println("��������" + result);
						// ����json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDesc = jo.getString("description");
						mDownLoadUrl = jo.getString("downloadUrl");
						// ���json�������
						System.out.println("�汾����" + mVersionName + ",�汾�ţ�"
								+ mVersionCode + ",�汾������" + mDesc + ",���ص�ַ��"
								+ mDownLoadUrl);

						// �Д��Ƿ��и���
						if (mVersionCode > getVersionCode()) {
							// ��������VersionCode���ڱ��ص�VersionCode
							// ˵���и��£����������Ի���
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// URL����
					msg.what = CODE_UPDATE_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// �������
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// json�����쳣
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;// �������绨�ѵ�ʱ��
					if (timeUsed < 2000) {
						// ��֤splashչʾ2��
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
	 * �����Ի���
	 */
	protected void showUpdateDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("��������");
				download();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * ����apk�ļ�
	 */
	protected void download() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			// ʹ��XUtils
			HttpUtils utils = new HttpUtils();
			utils.download(mDownLoadUrl, target, new RequestCallBack<File>() {

				// �����ļ��Ľ���
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					System.out.println("���ؽ���" + current + "/" + total);
				}

				// ���سɹ�
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					System.out.println("���سɹ�");
				}

				// ����ʧ��
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "����ʧ��",
							Toast.LENGTH_LONG).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "û���ҵ�sd��", Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * ����������
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
