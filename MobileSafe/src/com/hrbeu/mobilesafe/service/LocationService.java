package com.hrbeu.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 获取经纬度坐标的service
 * 
 * @author Hankai Xia
 * 
 */
public class LocationService extends Service {

	private LocationManager lm;
	private MyLocationListener listener;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		// 获取所有位置提供者
		// List<String> allProviders = lm.getAllProviders();
		// System.out.println(allProviders);
		Criteria criteria = new Criteria();
		// 是否允许付费，比如使用3g网络定位
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 获取最佳位置提供者
		String bestProvider = lm.getBestProvider(criteria, true);

		listener = new MyLocationListener();
		// 参数1表示位置提供者，参数2表示最短更新时间，参数3表示最短更新距离
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);
	}

	class MyLocationListener implements LocationListener {
		// 位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			// 经度坐标
			// String longitude = "经度：" + location.getLongitude();
			// 纬度坐标
			// String latitude = "纬度：" + location.getLatitude();
			// 将获取的经纬度保存在sp中

			System.out.println("get location!!");
			mPref.edit()
					.putString(
							"location",
							"j:" + location.getLongitude() + ";w:"
									+ location.getLatitude()).commit();
			// 停掉service
			stopSelf();

			// 精确度
			// String accuracy = "精确度：" + location.getAccuracy();
			// 海拔
			// String altitude = "海拔：" + location.getAltitude();

		}

		// 位置提供者状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}

		// 用户打开gps
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}

		// 用户关闭gps
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 当activity销毁时，停止更新位置，节省电量
		lm.removeUpdates(listener);
	}

}
