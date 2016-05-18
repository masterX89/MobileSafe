package com.hrbeu.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.db.dao.AddressDao;

/**
 * 来电提示
 * 
 * @author Hankai Xia
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;
	private int startX;
	private int startY;
	private WindowManager.LayoutParams params;
	private int winWidth;
	private int winHeight;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		// PhoneStateListener listener = new PhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);// 监听打电话的状态
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);// 动态注册广播
	}

	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话铃响
				System.out.println("电话铃响");
				String address = AddressDao.getAddress(incomingNumber);// 返回来电号码归属地
				// Toast.makeText(AddressService.this, address,
				// Toast.LENGTH_LONG).show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 电话闲置
				if (mWM != null && view != null) {
					mWM.removeView(view);// 从Windows移除
					view = null;
				}
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 去电广播接受者 动态注册
	 * 
	 * 权限：android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author Hankai Xia
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String number = getResultData();
			String address = AddressDao.getAddress(number);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}

	}

	/**
	 * 自定义归属地浮窗
	 * 
	 * 需要权限android.permission.SYSTEM_ALERT_WINDOW
	 * 
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		// 獲取屏幕寬高
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();
		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.gravity = Gravity.LEFT + Gravity.TOP;// 重心位置设置为左上方(0,0)
		params.setTitle("Toast");

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		// 设置浮窗位置，基于左上方的偏移量
		params.x = lastX;
		params.y = lastY;

		// view = new TextView(this);
		view = View.inflate(this, R.layout.toast_address, null);
		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = mPref.getInt("address_style", 0);// 读取保存的style
		view.setBackgroundResource(bgs[style]);// 根据存储的style样式更新背景
		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);
		// tvText.setTextColor(Color.RED);
		mWM.addView(view, params);// 将view添加到了Windows上

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					// 更新浮窗位置
					params.x += dx;
					params.y += dy;
					// 防止座標偏離屏幕
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}
					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}
					mWM.updateViewLayout(view, params);
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					Editor edit = mPref.edit();
					edit.putInt("lastX", params.x);
					edit.putInt("lastY", params.y);
					edit.commit();
					break;

				default:
					break;
				}

				return true;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// 停止来电监听
		unregisterReceiver(receiver);// 注销广播
	}
}
