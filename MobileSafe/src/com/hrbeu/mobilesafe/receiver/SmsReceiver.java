package com.hrbeu.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.widget.Toast;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.service.LocationService;

/**
 * 拦截短信
 *
 * @author Hankai Xia
 */
public class SmsReceiver extends BroadcastReceiver {

	private DevicePolicyManager mDPM;
	private ComponentName mDeviceAdminSample;

	@Override
	public void onReceive(Context context, Intent intent) {
		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);// 获取设备策略服务
		mDeviceAdminSample = new ComponentName(context, AdminReceiver.class);// 设备管理组件

		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		for (Object object : objects) {
			// 短信最多是140字节，超出会分为多条短信发送所以使用数组
			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			// 短信来源号码
			String originatingAddress = message.getOriginatingAddress();
			// 短信内容
			String messageBody = message.getMessageBody();

			System.out.println(originatingAddress + ":" + messageBody);

			if ("#*alarm*#".equals(messageBody)) {
				// 播放报警音乐，手机静音依然可以播放，使用媒体声音的通道和铃声无关
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();
				// 中断短信的传递，从而系统短信app就无法收到内容
				abortBroadcast();
			} else if ("#*location*#".equals(messageBody)) {
				// 获取经纬度坐标
				// 开启定位服务
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String location = sp.getString("location", "getting location...");
				System.out.println("location:" + location);

				// 读取安全号码
				String phone = sp.getString("safe_phone", "");
				// 发送短信给安全号码
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(phone, null, location, null, null);

				abortBroadcast();
			} else if ("#*wipedata*#".equals(messageBody)) {
				if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
					mDPM.wipeData(0);// 清除数据,恢复出厂设置
				} else {
					//UIUtils.showToast(context, "必须先激活设备管理器!");
					Toast.makeText(context, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
				}
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(messageBody)) {
				if (mDPM.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
					mDPM.lockNow();// 立即锁屏
					mDPM.resetPassword("123456", 0);
				} else {
					Toast.makeText(context, "必须先激活设备管理器!", Toast.LENGTH_SHORT).show();
					//UIUtils.showToast(context, "必须先激活设备管理器!");
				}
				abortBroadcast();
			}
		}
	}

}
