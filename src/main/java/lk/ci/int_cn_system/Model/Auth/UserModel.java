package lk.ci.int_cn_system.Model.Auth;

import java.util.List;

public class UserModel {
	private String id;

	private String username;

	private String email;

	private String phone;

	private String name;

	private String businessTitle;

	private String epfNo;
	
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	private List<RolesModel> roles;
	
	private LocationModel location;	
	
	public void setLocation(LocationModel location) {
		this.location = location;
	}
	public LocationModel getLocation() {
		return location;
	}

	public List<RolesModel> getRoles() {
		return roles;
	}

	public void setRoles(List<RolesModel> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBusinessTitle() {
		return businessTitle;
	}

	public void setBusinessTitle(String businessTitle) {
		this.businessTitle = businessTitle;
	}

	public String getEpfNo() {
		return epfNo;
	}

	public void setEpfNo(String epfNo) {
		this.epfNo = epfNo;
	}


	
	

}
