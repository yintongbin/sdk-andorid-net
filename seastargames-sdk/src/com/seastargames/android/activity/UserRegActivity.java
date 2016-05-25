package com.seastargames.android.activity;

import com.seastargames.android.eunm.UserLoginChanneModel;
import com.seastargames.android.sdk.UserManager;
import com.seastargames.sdk.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserRegActivity extends Activity implements View.OnClickListener{

	UserManager userManger = new UserManager();
	UserLoginChanneModel userLoginChanneModel = new UserLoginChanneModel();
	
	EditText UserName;
	EditText PassWord;
	EditText RePassWord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssgame_account_register);

		Button btnUserReg = (Button) findViewById(R.id.u2_account_register);
		btnUserReg.setOnClickListener(this);
 
		UserName =(EditText)findViewById(R.id.u2_account_register_nickname);
		PassWord =(EditText)findViewById(R.id.u2_account_register_password);
		RePassWord =(EditText)findViewById(R.id.u2_account_register_email);
		
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(v.getId() == R.id.u2_account_register)
		{
			String userName = UserName.getText().toString();
			String passWord = PassWord.getText().toString();
			
			userManger.LoginByUser(userName,passWord,1,new UserManager.LoginByUserCallBack() {
				 
				@Override
				public void LoginByUserCallBack(String graph) {
					// TODO 自动生成的方法存根
					
				}
			});
		}
		
	}

}
