package com.seastargames.android.activity;

import com.seastargames.android.sdk.UserManager;
import com.seastargames.sdk.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserRePassWordActivity extends Activity implements View.OnClickListener{

	EditText PassWord;
	EditText RePassWord;
	UserManager userManger = new UserManager();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssgame_change_password);

		Button btnUpdatePWD = (Button) findViewById(R.id.u2_title_bar_button_right);
		btnUpdatePWD.setOnClickListener(this);
		PassWord =(EditText)findViewById(R.id.u2_account_login_account);
		RePassWord =(EditText)findViewById(R.id.u2_account_login_password); 
		
	}

	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(v.getId() == R.id.u2_title_bar_button_right)
		{
			String password =  PassWord.getText().toString();
			String Repassword =  RePassWord.getText().toString();
		}
		
		
	}

}
