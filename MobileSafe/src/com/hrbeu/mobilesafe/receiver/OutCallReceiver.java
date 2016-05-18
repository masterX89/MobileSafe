package com.hrbeu.mobilesafe.receiver;

import com.hrbeu.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * 去电广播接受者
 * 
 * 权限：android.permission.PROCESS_OUTGOING_CALLS
 * 
 * @author Hankai Xia
 * 
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		String address = AddressDao.getAddress(number);
		Toast.makeText(context, address, Toast.LENGTH_LONG).show();

	}

}
