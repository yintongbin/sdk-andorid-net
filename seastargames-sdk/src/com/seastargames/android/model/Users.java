package com.seastargames.android.model;

public class Users {
	private String username;
	private String password;
	private String thirdUserId; 
	private String session;
	
	public String getUsername() {
		return username;
	}

	public Users setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Users setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getThirdUserId() {
		return thirdUserId;
	}

	public Users setThirdUserId(String thirdUserId) {
		this.thirdUserId = thirdUserId;
		return this;
	}

	public String getSession() {
		return session;
	}

	public Users setSession(String session) {
		this.session = session;
		return this;
	}
}
