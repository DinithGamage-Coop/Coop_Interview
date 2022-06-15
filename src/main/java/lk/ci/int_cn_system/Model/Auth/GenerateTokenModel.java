package lk.ci.int_cn_system.Model.Auth;

import java.util.List;

//import lk.ci.int_cn_system.Model.Setting.DistrictModel;

public class GenerateTokenModel {
	
	private String password;
	
	private String username;	

	private LoginSaveRequestModel loginSaveRequest;


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LoginSaveRequestModel getLoginSaveRequest() {
		return loginSaveRequest;
	}

	public void setLoginSaveRequest(LoginSaveRequestModel loginSaveRequest) {
		this.loginSaveRequest = loginSaveRequest;
	}

 
	

}
