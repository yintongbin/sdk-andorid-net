package com.seastargames.android.sdk;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.seastargames.android.sdk.iabutil.Information;
import com.seastargames.android.sdk.iabutil.MD5;

public class Pay {

	public interface PayCallBack {
		void PayCallBack(String rtnStr);
	}

	Google google = new Google();
	Facebook fb = new Facebook();
	MD5 md5 = new MD5();
	Information information = new Information();
	private String key = "";
	private String appid;
	private String deviceId;
	private String locale;
	private String Url = "";
	private String TAG;
	private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType
			.parse("text/x-markdown; charset=utf-8");

	public void GoogleIapReg(long userId, String session, String productId,
			String googleOriginalJson, String googleSignature,
			String gameRoleId, String cparam, String proceAmountMicros,
			String proceCurrencyCode, final PayCallBack listener) {
		JSONArray jsonMembers = new JSONArray();
		JSONObject msg = new JSONObject();
		try {
			msg.put("appid", userId);
			msg.put("userId", userId);
			msg.put("session", session);
			msg.put("productId", productId);
			msg.put("googleOriginalJson", googleOriginalJson);
			msg.put("gameRoleId", gameRoleId);
			msg.put("cparam", cparam);
			msg.put("proceAmountMicros", proceAmountMicros);
			msg.put("proceCurrencyCode", proceCurrencyCode);
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
				listener.PayCallBack(responseCall);
				Log.d(TAG, responseCall);
			}

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO 自动生成的方法存根

			}
		});
	}

}
