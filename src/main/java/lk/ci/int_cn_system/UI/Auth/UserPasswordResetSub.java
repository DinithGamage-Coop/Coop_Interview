package lk.ci.int_cn_system.UI.Auth;


import java.sql.Connection;
import java.sql.Statement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.vaadin.dialogs.ConfirmDialog;

import lk.ci.int_cn_system.Utils.JDBC_COOP;
import lk.ci.int_cn_system.Utils.MD5;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;

public class UserPasswordResetSub extends UserPasswordReset {

	String namm;
	String idd;
	String username;
	String epf;
//	String token = "";

	public UserPasswordResetSub() {
		// TODO Auto-generated method stub

		button_change_pw.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (passwordField_new.getValue().equals("")) {

					Notification.show("Enter a password");

				} else if (passwordField_confirm.getValue().trim().equals(passwordField_new.getValue().trim())) {

					
					ConfirmDialog.show(getUI(), "Confirmation", "Are you sure to confirm", "Yes", "No",
							new ConfirmDialog.Listener() {
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {		
										
										try {
											SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											Connection co = JDBC_COOP.con();
											Statement st =  co.createStatement();
											st.executeUpdate("update user_login set PASSWORD='"+MD5.cryptWithMD5(passwordField_confirm.getValue().toUpperCase())+"',MODIFIED_DATE='"+df.format(new Date())+"',MODIFIED_BY='"+username+"' where USERNAME='"+username+"'");
											st.close();
											co.close();
											Notification.show("Your password has been changed successfully");
											Page.getCurrent().open("http://172.20.10.16:8080/COOP_INTERVIEW/", null);
											
										
										} catch (Exception e) {
											e.printStackTrace();
											// TODO: handle exception
										}
										

										getSession().close(); 

									}
								}
							});

				}

				else {
					Notification.show("Those passwords didn’t match. Try again.");
					String id = passwordField_new.getValue();
					// getById(id);

				}

			}
		});

	}
//


	public void setUserDetails(String user_name) {
		// TODO Auto-generated method stub
		username = user_name;
		usernamet.setValue(user_name);
	}

}
