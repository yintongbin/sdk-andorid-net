package com.seastargames.android.sdk.iabutil;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class Information {
   
	
	
	public String GetId(Context context) {
		
		
		String result = "";
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {

			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			result = wifiInfo.getMacAddress();
			Log.i("text", "手机macAdd:" + result);
		} else {
			try {
				TelephonyManager mTm = (TelephonyManager) context
						.getSystemService(context.TELEPHONY_SERVICE);
				result = mTm.getDeviceId();
			} catch (Exception ex) {
				result = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
		}
		return result;
	}

	
}
