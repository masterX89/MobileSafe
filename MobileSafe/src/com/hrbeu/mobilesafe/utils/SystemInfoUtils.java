package com.hrbeu.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

public class SystemInfoUtils {
	/**
	 * 判断一个服务是否处于运行状态
	 *
	 * @param context 上下文
	 * @return
	 */
	public static boolean isServiceRunning(Context context, String className) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(200);
		for (RunningServiceInfo info : infos) {
			String serviceClassName = info.service.getClassName();
			if (className.equals(serviceClassName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 返回进程的总个数
	 *
	 * @param context
	 * @return
	 */
	public static int getProcessCount(Context context) {
		// 得到进程管理者
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		// 获取到当前手机上面所有运行的进程
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		// 获取手机上面一共有多少个进程
		return runningAppProcesses.size();
	}

	/**
	 * 获取到剩余内存
	 *
	 * @param context
	 * @return
	 */
	public static long getAvailMem(Context context) {
		// 得到进程管理者
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		// 获取到内存的基本信息
		activityManager.getMemoryInfo(memoryInfo);
		// 获取到剩余内存
		return memoryInfo.availMem;
	}

	public static long getTotalMem(Context context) {
		// 获取到总内存
		// 得到进程管理者
		ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		// 获取到内存的基本信息
		activityManager.getMemoryInfo(memoryInfo);
		// 获取到剩余内存
		return memoryInfo.totalMem;


	}

}
