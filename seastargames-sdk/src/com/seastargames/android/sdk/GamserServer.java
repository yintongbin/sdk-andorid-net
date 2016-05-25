package com.seastargames.android.sdk;

import java.security.MessageDigest;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.seastargames.android.sdk.iabutil.Information;
import com.seastargames.android.sdk.iabutil.MD5; 
import com.seastargames.sdk.R;

public class GamserServer extends Activity {

	Google google = new Google();
	Facebook fb = new Facebook();
	MD5 md5 = new MD5();
	Information information = new Information();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssgame_account_login);
	}

	public void allInit(Activity activity) {
		google.init(new Google.OnInitCallBack() {

			@Override
			public void onInitCallBack(boolean success) {
				// TODO 自动生成的方法存根

			}
		});
		fb.init(new Facebook.OnInitCallBack() {

			@Override
			public void onInitCallBack(boolean isInit) {
				// TODO 自动生成的方法存根

			}
		});
	}

	public void setCurActivity(Activity activity) {
		fb.setCurActivity(activity);
		google.setCurActivity(activity);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		fb.onActivityResult(requestCode, resultCode, data);
		google.onActivityResult(requestCode, resultCode, data);
	}

}
