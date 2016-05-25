package com.seastargames.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.seastargames.android.eunm.UserLoginChanneModel;
import com.seastargames.android.sdk.Facebook;
import com.seastargames.android.sdk.Google;
import com.seastargames.android.sdk.UserManager;
import com.seastargames.android.sdk.Google.GoogleUserModel;
import com.seastargames.sdk.R;

public class UserLoginActivity extends Activity implements View.OnClickListener {

	UserManager userManger = new UserManager(this);
	Google google = new Google();
	Facebook fb = new Facebook();
	UserLoginChanneModel userLoginChanneModel = new UserLoginChanneModel();
	EditText UserName;
	EditText PassWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssgame_account_login);

		Button btnUserLogin = (Button) findViewById(R.id.u2_account_login_log);
		btnUserLogin.setOnClickListener(this);
		Button btnUserReg = (Button) findViewById(R.id.u2_account_login_reg);
		btnUserReg.setOnClickListener(this);
		Button btnUserGuest = (Button) findViewById(R.id.u2_account_guest_log);
		btnUserGuest.setOnClickListener(this);
		Button btnfbLogin = (Button) findViewById(R.id.u2_fbooklogin);
		btnUserGuest.setOnClickListener(this);
		Button btnGoogleLogin = (Button) findViewById(R.id.u2_emaillogin);
		btnGoogleLogin.setOnClickListener(this);
		
		UserName =(EditText)findViewById(R.id.u2_account_login_account);
		PassWord =(EditText)findViewById(R.id.u2_account_login_password); 
	}
 
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		// 登录
		if (v.getId() == R.id.u2_account_login_log) {

			String userName = UserName.getText().toString();
			String passWord = PassWord.getText().toString();
			
			userManger.LoginByUser(userName,passWord,0,new UserManager.LoginByUserCallBack() {
				
				@Override
				public void LoginByUserCallBack(String graph) {
					// TODO 自动生成的方法存根
					
				}
			});
		}
		// 注册
		else if (v.getId() == R.id.u2_account_login_reg) {

		}
		// 快速登录
		else if (v.getId() == R.id.u2_account_guest_log) {
			userManger.AuthLogin(new UserManager.AuthLoginCallBack() {

				@Override
				public void AuthLoginCallBack(String graph) {
					// TODO 自动生成的方法存根

				}
			});
		}
		// facebool登录
		else if (v.getId() == R.id.u2_fbooklogin) {
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
		else if(v.getId() == R.id.u2_emaillogin)
		{
			google.login(new Google.OnLoginCallBack() {
				
				@Override
				public void onLoginCallBack(boolean success, GoogleUserModel googleUserModel) {
					// TODO 自动生成的方法存根
					if(success)
					{
						userManger.LoginByTridParty(googleUserModel.id.toString(),"",userLoginChanneModel.TYPE_GOOGLE,new UserManager.LoginByTridPartyCallBack() {
							
							@Override
							public void LoginByTridPartyCallBack(String graph) {
								// TODO 自动生成的方法存根
								
							}
						});
						
					}
					
				}
			});
		}
	}
}
