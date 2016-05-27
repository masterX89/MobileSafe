package com.hrbeu.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.gsm.SmsMessage;

import com.hrbeu.mobilesafe.db.dao.BlackNumberDao;

public class CallSafeService extends Service {

	private BlackNumberDao dao;

	public CallSafeService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BlackNumberDao(this);
		//初始化短信广播
		InnerReceiver innerReceiver = new InnerReceiver();
		IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(Integer.MAX_VALUE);
		registerReceiver(innerReceiver, intentFilter);
	}

	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("接收到短信");
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				// 短信最多是140字节，超出会分为多条短信发送所以使用数组
				SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
				// 短信来源号码
				String originatingAddress = message.getOriginatingAddress();
				// 短信内容
				String messageBody = message.getMessageBody();
				//通过短信的电话号码查询拦截模式
				String mode = dao.findNumber(originatingAddress);
				/**
				 * 黑名单拦截方式
				 * 1。全部拦截 电话拦截+短信拦截
				 * 2。电话拦截
				 * 3.短信拦截
				 */
				if (mode.equals("1")) {
					abortBroadcast();
				} else if (mode.equals("3")) {
					abortBroadcast();
				}
				//智能拦截，需要匹配数据库 分词技术
				if (messageBody.contains("学生妹")) {
					abortBroadcast();
				}

			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
