package lk.ci.int_cn_system.Model.Auth;

public class RoleCheck {

	
	public boolean isAdmin(UserResponseModel user) {
		
		boolean admin=false;
		
		for(RolesModel role:user.getUser().getRoles()) {
			System.out.println(role.getName());
			if(role.getName().equalsIgnoreCase("admin")) {
				admin=true;
				break;
			}
		}
		
		return admin;
	}
	
	
}
