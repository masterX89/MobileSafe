package com.hrbeu.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.gsm.SmsMessage;

import com.hrbeu.mobilesafe.R;
import com.hrbeu.mobilesafe.service.LocationService;

/**
 * 拦截短信
 * 
 * @author Hankai Xia
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
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
				SharedPreferences sp = context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
				String location = sp.getString("location",
						"getting location...");
				System.out.println("location:" + location);
				abortBroadcast();
			} else if ("#*wipedata*#".equals(messageBody)) {
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(messageBody)) {
				abortBroadcast();
			}
		}
	}

}
