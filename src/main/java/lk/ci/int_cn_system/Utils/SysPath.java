package lk.ci.int_cn_system.Utils;

public class SysPath {

	
	/*public static void main(String [] args) {
		
		GetSysPath();
		
	}*/
	
	
	public static String GetSysPath() {
		
		try {
			
		String s=System.getProperty("os.name");
		
	//	System.out.println(s);
			
		String s_path="C:/APPS/";
		
		if(s.toLowerCase().startsWith("linux")) {
			
			s_path="/home/ciclsw15/APPS/";
			
		}
		
		
		
			return s_path;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
}
