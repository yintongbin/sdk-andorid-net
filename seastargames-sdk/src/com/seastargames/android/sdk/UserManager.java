package com.seastargames.android.sdk;

import java.io.IOException;
import java.math.BigInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.seastargames.android.sdk.iabutil.Information;
import com.seastargames.android.sdk.iabutil.MD5;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class UserManager {
	
	public UserManager(Context context) {
		// TODO 自动生成的构造函数存根
	}
	
	public interface AuthLoginCallBack {
		void AuthLoginCallBack(String rtnStr);
	}

	public interface LoginByUserCallBack {
		void LoginByUserCallBack(String rtnStr);
	}

	public interface LoginByTridPartyCallBack {
		void LoginByTridPartyCallBack(String rtnStr);
	}

	public interface BindQueryRegCallBack {
		void BindQueryRegCallBack(String rtnStr);
	}

	public interface BindRequestCallBack {
		void BindRequestCallBack(String rtnStr);
	}

	public interface UnBindRequestCallBack {
		void UnBindRequestCallBack(String rtnStr);
	}
 
	
	Google google = new Google();
	Facebook fb = new Facebook();
	MD5 md5 = new MD5(); 
	Information information = new Information();
	private String key = "123456";
	private String appid = "1";
	private String deviceId = "1";
	private String locale = "1";
	private String Url = "http://10.10.10.21:8080/auth/guest";
	private String TAG;
	private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("text/x-markdown; charset=utf-8");



	// 快速登录
	public void AuthLogin(final AuthLoginCallBack listener) {
		deviceId = information.GetId(context);
//		locale = getResources().getConfiguration().locale.getLanguage();
//		String deviceInfo = android.os.Build.MODEL + ";"
//				+ android.os.Build.VERSION.SDK + ";"
//				+ android.os.Build.VERSION.RELEASE;
		String deviceInfo = "1";
		String resign = appid + deviceId + key;  
		String sign = md5.MD5(resign);
		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("appid", key);
			msg.put("deviceId", deviceId);
			msg.put("locale", locale);
			msg.put("deviceInfo", deviceInfo);
			msg.put("sign", sign);
		} catch (Exception ex) {

		}
		jsonMembers.put(msg);

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Url)
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
						jsonMembers.toString())).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				listener.AuthLoginCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}

	// 账号密码登录
	public void LoginByUser(String UserName, String PassWord, int Regist,
			final LoginByUserCallBack listener) {

		deviceId = information.GetId(context);
		locale = context.getResources().getConfiguration().locale.getLanguage();
		String deviceInfo = android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.SDK + ";"
				+ android.os.Build.VERSION.RELEASE;
		String username = UserName;
		String password = PassWord;
		String resign = appid + deviceId + username + password + key;
		String sign = md5.MD5(resign);

		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("appid", key);
			msg.put("deviceId", deviceId);
			msg.put("locale", locale);
			msg.put("deviceInfo", deviceInfo);
			msg.put("username", username);
			msg.put("password", password);
			msg.put("sign", sign);
			msg.put("regist", Regist);
		} catch (Exception ex) {

		}
		jsonMembers.put(msg);

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Url)
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
						jsonMembers.toString())).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				listener.LoginByUserCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}

	// 第三方登录
	public void LoginByTridParty(String ThirdUserId, String ThirdAccessToken,
			int LoginType, final LoginByTridPartyCallBack listener) {

		deviceId = information.GetId(context);
		locale = context.getResources().getConfiguration().locale.getLanguage();
		String deviceInfo = android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.SDK + ";"
				+ android.os.Build.VERSION.RELEASE;
		String thirdUserId = ThirdUserId;
		String thirdAccessToken = ThirdAccessToken;
		int loginType = LoginType;
		String resign = appid + deviceId + thirdUserId + thirdUserId
				+ thirdAccessToken + loginType + key;
		String sign = md5.MD5(resign);

		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("appid", key);
			msg.put("deviceId", deviceId);
			msg.put("locale", locale);
			msg.put("deviceInfo", deviceInfo);
			msg.put("thirdUserId", thirdUserId);
			msg.put("thirdAccessToken", thirdAccessToken);
			msg.put("loginType", loginType);
			msg.put("sign", sign);
		} catch (Exception ex) {

		}
		jsonMembers.put(msg);
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Url)
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
						jsonMembers.toString())).build();
		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				listener.LoginByTridPartyCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}

	// 获取绑定关系
	public void BindQueryReg(String session, int userId,
			final BindQueryRegCallBack listener) {

		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("session", session);
			msg.put("userId", userId);
		} catch (Exception ex) {
		}

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Url)
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
						jsonMembers.toString())).build();

		client.newCall(request).enqueue(new okhttp3.Callback() {

			@Override
			public void onResponse(Call arg0, Response response)
					throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				listener.BindQueryRegCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根
			}
		});
	}
	//账号绑定
	public void BindRequest(String session, long userId, String thirdUserId,
			int loginType, final BindRequestCallBack listener) {

		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("appId", appid);
			msg.put("session", session);
			msg.put("userId", userId);
			msg.put("thirdUserId", thirdUserId);
			msg.put("loginType", loginType);
		} catch (Exception ex) {
		}
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Url)
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
						jsonMembers.toString())).build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				listener.BindRequestCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}
	//取消绑定
	public void unBindRequest(String session,long userId,String thirdUserId,int loginType ,final UnBindRequestCallBack listener)
	{
		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("appId", appid);
			msg.put("session", session);
			msg.put("userId", userId);
			msg.put("thirdUserId", thirdUserId);
			msg.put("loginType", loginType);
		} catch (Exception ex) {
		}
		
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(Url)
				.post(RequestBody.create(MEDIA_TYPE_MARKDOWN,
						jsonMembers.toString())).build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO 自动生成的方法存根
				String responseCall = response.body().string();
				listener.UnBindRequestCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}
	  
}
