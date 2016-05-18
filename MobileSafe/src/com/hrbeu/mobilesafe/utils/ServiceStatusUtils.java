package com.hrbeu.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务状态工具类
 * 
 * @author Hankai Xia
 * 
 */
public class ServiceStatusUtils {
	public static boolean isServiceRunning(Context ctx, String serviceName) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取100个系统正在运行的服务
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			// 获取服务名称
			String className = runningServiceInfo.service.getClassName();
			// System.out.println(className);
			if (className.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
