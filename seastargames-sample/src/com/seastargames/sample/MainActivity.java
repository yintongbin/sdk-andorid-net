package com.seastargames.sample;

import com.seastargames.SeaStar;
import com.seastargames.SeaStarListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.seastargames.sample.R;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

	private static String TAG = "SeaStarDemo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SeaStar.Instance().Init(this, new SeaStarListener() {

			@Override
			public void onPaySuccess(String Order, String sku) {
				// TODO 自动生成的方法存根
				Log.d(TAG, "支付成功");
				Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onPayFail(String sku) {
				// TODO 自动生成的方法存根
				Log.d(TAG, "支付失败");
				Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onLoginSuccess(long UserId) {
				// TODO 自动生成的方法存根
				Log.d(TAG, "登陆成功:UserId" + UserId);
				Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onLoginFail() {
				// TODO 自动生成的方法存根
				Log.d(TAG, "登录失败");
				Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG)
						.show();
			}
		});
		Button btnUserLogin = (Button) findViewById(R.id.loginbutton);
		btnUserLogin.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.loginbutton) {
			SeaStar.Instance().Login(this);
		} else if (v.getId() == R.id.userManagerbutton) {
			SeaStar.Instance().UserManager(this);
		} else if (v.getId() == R.id.paybutton) {

		}

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		SeaStar.Instance().onActivityResult(requestCode, resultCode, data);
	}

}
