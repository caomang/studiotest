package cn.sbx.deeper.moblie.util;

public class UserInfo {

	private UserInfo() {}
	
	private static UserInfo instance = new UserInfo();
	
	public static UserInfo getInstance() {
		return instance;
	}
	
	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
