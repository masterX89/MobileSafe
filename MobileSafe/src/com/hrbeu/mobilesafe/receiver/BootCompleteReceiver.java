package com.hrbeu.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 监听手机开机启动的广播
 * 
 * @author Hankai Xia
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		boolean protect = sp.getBoolean("protect", false);
		// 只有在防盗保护开启的前提下才进行SIM卡判断
		if (protect) {
			// 获取绑定的SIM卡
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				// 获取当前手机的SIM卡
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				// 拿到当前的SIM卡
				String currentSim = tm.getSimSerialNumber();
				if (sim.equals(currentSim)) {
					System.out.println("手机安全");
				} else {
					System.out.println("SIM卡已经变化，发送报警短信");
				}
			}
		}
	}
}
