package lk.ci.int_cn_system.UI.SimQuotation;

public class SysPath {

	public static String GetSysPath() {

		try {

			String s = System.getProperty("os.name");
			String s_path = "C:/APPS/";
			if (s.toLowerCase().startsWith("linux")) {
				s_path = "/home/ciclsw15/APPS/";
			}
			return s_path;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

}
