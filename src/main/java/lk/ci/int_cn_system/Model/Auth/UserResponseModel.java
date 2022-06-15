package lk.ci.int_cn_system.Model.Auth;

import java.util.List;

public class UserResponseModel {	
	
	private String token;
	
	private UserModel user;	

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
