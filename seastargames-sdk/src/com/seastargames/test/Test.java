package com.seastargames.test;

import com.seastargames.android.sdk.UserManager;
import com.seastargames.android.sdk.UserManager.AuthLoginCallBack;


public class Test {
	UserManager um = new UserManager();
	public void main(String[] args) {
		um.AuthLogin(new AuthLoginCallBack() {
			
			@Override
			public void AuthLoginCallBack(String rtnStr) {
				// TODO 自动生成的方法存根
				System.out.println(rtnStr);
			}
		});
	}
}
