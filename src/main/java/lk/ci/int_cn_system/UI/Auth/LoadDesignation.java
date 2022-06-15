package lk.ci.int_cn_system.UI.Auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import lk.ci.int_cn_system.Utils.JDBC_MY;

public class LoadDesignation extends EmpApprovalSub {
	
	public void designation_load() {
	try {
		Connection co = JDBC_MY.con();
		Statement st = co.createStatement();
		ResultSet re = st.executeQuery("SELECT * FROM designations");
		while (re.next()) {
			comboBox_designation.addItem(re.getString("designation"));
		}
		re.close();
		st.close();
		co.close();
	} catch (Exception e) {
		e.printStackTrace();
		// TODO: handle exception
	}

}

}
