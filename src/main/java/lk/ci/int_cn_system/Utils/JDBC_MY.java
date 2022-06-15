package lk.ci.int_cn_system.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC_MY {
	
//	static String  url="jdbc:mysql://172.20.10.20:3306/e_details?zeroDateTimeBehavior=convertToNull";
	static String  url="jdbc:mysql://localhost:3306/e_details?useSSL=false";



	    public static Connection con() throws Exception {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection c=DriverManager.getConnection(url,"root", "1234");
//	        Connection c=DriverManager.getConnection(url,"root", "freaks");
	        return c;
	    }
	    
}
