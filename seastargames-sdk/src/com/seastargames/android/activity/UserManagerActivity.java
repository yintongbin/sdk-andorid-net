package com.seastargames.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.seastargames.android.eunm.UserLoginChanneModel;
import com.seastargames.android.sdk.Facebook;
import com.seastargames.android.sdk.Google;
import com.seastargames.android.sdk.UserManager;
import com.seastargames.android.sdk.Google.GoogleUserModel;
import com.seastargames.sdk.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserManagerActivity extends Activity implements
		View.OnClickListener {
	UserManager userManger = new UserManager();
	Google google = new Google();
	Facebook fb = new Facebook();
	UserLoginChanneModel userLoginChanneModel = new UserLoginChanneModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssgame_account_man);

		Button btnUpdatePWD = (Button) findViewById(R.id.u2_account_update_pass);
		btnUpdatePWD.setOnClickListener(this);
		Button btnFindPWD = (Button) findViewById(R.id.u2_account_find_pass);
		btnFindPWD.setOnClickListener(this);
		Button btnBindEmail = (Button) findViewById(R.id.u2_account_bind_email);
		btnBindEmail.setOnClickListener(this);
		Button btnBindGoogle = (Button) findViewById(R.id.u2_account_bind_google);
		btnBindGoogle.setOnClickListener(this);
		Button btnBindFacebook = (Button) findViewById(R.id.u2_account_bind_facebook);
		btnBindFacebook.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (v.getId() == R.id.u2_account_update_pass) {
			Intent intenr = new Intent(UserManagerActivity.this,
					UserRePassWordActivity.class);

		} else if (v.getId() == R.id.u2_account_find_pass) {

		} else if (v.getId() == R.id.u2_account_bind_email) {

		} else if (v.getId() == R.id.u2_account_bind_google) {

			google.login(new Google.OnLoginCallBack() {

				@Override
				public void onLoginCallBack(boolean success,
						GoogleUserModel googleUserModel) {
					// TODO 自动生成的方法存根
					if (success) {
						userManger.LoginByTridParty(
								googleUserModel.id.toString(), "",
								userLoginChanneModel.TYPE_GOOGLE,
								new UserManager.LoginByTridPartyCallBack() {

									@Override
									public void LoginByTridPartyCallBack(
											String graph) {
										// TODO 自动生成的方法存根

									}
								});

					}

				}
			});

		} else if (v.getId() == R.id.u2_account_bind_facebook) {
			fb.login(new Facebook.OnLoginCallBack() {

				@Override
				public void onLoginCallBack(boolean isLogin, String user) {
					// TODO 自动生成的方法存根
					if (isLogin) {

						try {
							JSONObject fbUser = new JSONObject(user);

							userManger.LoginByTridParty(fbUser.getString("id")
									.toString(), "",
									userLoginChanneModel.TYPE_FACEBOOK,
									new UserManager.LoginByTridPartyCallBack() {

										@Override
										public void LoginByTridPartyCallBack(
												String graph) {
											// TODO 自动生成的方法存根

										}
									});

						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}

					}

				}
			});
		}
	}

}
