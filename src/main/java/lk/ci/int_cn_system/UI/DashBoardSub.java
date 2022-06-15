package lk.ci.int_cn_system.UI;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.vaadin.dialogs.ConfirmDialog;

import lk.ci.int_cn_system.Model.Auth.RoleCheck;
import lk.ci.int_cn_system.Model.Auth.UserResponseModel;
import lk.ci.int_cn_system.UI.Auth.BoardApprovalViewSub;
import lk.ci.int_cn_system.UI.Auth.EmpApprovalSub;
import lk.ci.int_cn_system.UI.Auth.EmpReports;
import lk.ci.int_cn_system.UI.Auth.EmpReportsSub;
import lk.ci.int_cn_system.UI.Auth.Promotions;
import lk.ci.int_cn_system.UI.Auth.PromotionsSub;
import lk.ci.int_cn_system.UI.Auth.UserPasswordResetSub;
import lk.ci.int_cn_system.UI.Auth.UserRegistrationSub;
import lk.ci.int_cn_system.Utils.ConstantData;
import lk.ci.int_cn_system.Utils.JDBC_MY;

import com.google.gson.Gson;
import com.lowagie.text.pdf.codec.Base64.InputStream;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DashBoardSub extends DashBoard {
	UI thisUI = UI.getCurrent().getUI();
	private String token;

	UserRegistrationSub userRegistrationSub;
	UserPasswordResetSub userPasswordResetSub;
	EmpApprovalSub empApprovalSub;
	BoardApprovalViewSub approvalViewSub;
	EmpReportsSub empreportsub;
	PromotionsSub promotionssub;

	private Window win;
	private String FullName;
	private DashBoardSub mainUI;

	Command commandSettings;
	Command commandProfile;

//	User loggedInUser;
//	UserResponseModel user = new UserResponseModel();

	public void getUserDetails(String user2, String fullName2, String branch, int level) {
		username = user2;
		full_name = fullName2;
		System.out.println(full_name);
		labelUserName.setValue(full_name);
		call_name(user2);
		
		FileResource dsh = new FileResource(new File("C:\\APPS\\app_icons\\HR.png"));
		image_dashbord.setSource(dsh);
		
		try {

			File photo = new java.io.File("\\\\172.20.10.20\\GENERAL\\pics\\" + username + ".png");
			if (photo.exists()) {

				FileResource fr = new FileResource(photo);
				fr.setCacheTime(0);

				imageProfile.setSource(fr);

			} else {

				FileResource fr = new FileResource(new File("C:/APPS/app_icons/facebook.png"));
				fr.setCacheTime(0);

				imageProfile.setSource(fr);
			}

//			labelUserName.setValue(loggedInUser.getUser().getName());
//			labelUserBranch.setValue(loggedInUser.getBranch());

		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();

		}

		treeMain.addValueChangeListener(new ValueChangeListener() {

			// @Override
			public void valueChange(ValueChangeEvent event) {

				if (treeMain.getValue() != null) {

					if (treeMain.getValue().toString().equals("Quotation Register")) {

						verticalLayout.removeAllComponents();

						// SimQuotationRegSub simQuotationRegSub = new SimQuotationRegSub();

						// simQuotationRegSub.sendDashBoard(DashBoardSub.this, loggedInUser);

						// verticalLayout.addComponent(simQuotationRegSub);

					} else if (treeMain.getValue().toString().equals("New Quotation")) {

						verticalLayout.removeAllComponents();

//						if (simQuotationSub == null) {
//
//							simQuotationSub = new SimQuotationSub();
//
//							simQuotationSub.setUser(loggedInUser);
//
//						}

//						verticalLayout.addComponent(simQuotationSub);

					}

					else if (treeMain.getValue().toString().equals("New Employee")) {
						
						try {
							Connection con = JDBC_MY.con();
							Statement st = con.createStatement();
							String sql = "SELECT user_leval FROM  interv_user where user_name='" + username + "'";
							ResultSet rs = st.executeQuery(sql);
							while (rs.next()) {
								Integer ulevl = rs.getInt("user_leval");
								if ((ulevl>=11)&&(ulevl>=49)) {
									System.out.println(ulevl);
//									Notification.show("Access denied");
//									Notification.show("Access denied",Notification.TYPE_ERROR_MESSAGE);
									 
									userRegistrationSub.setEnabled(false);

								} else {
									verticalLayout.removeAllComponents();

									if (userRegistrationSub == null) {

										userRegistrationSub = new UserRegistrationSub();

										userRegistrationSub.getUserDetails(username, full_name);

									}

									verticalLayout.addComponent(userRegistrationSub);

								}
							}
							rs.close();
							st.close();
							con.close();
						} catch (Exception e) {
							// TODO: handle exception
						}

						

					}

					else if (treeMain.getValue().toString().equals("HR Manager")) {
						try {
							Connection con = JDBC_MY.con();
							Statement st = con.createStatement();
							String sql = "SELECT user_leval FROM  interv_user where user_name='" + username + "'";
							ResultSet rs = st.executeQuery(sql);
							while (rs.next()) {
								Integer ulevl = rs.getInt("user_leval");
								if (ulevl >= 11) {
									System.out.println(ulevl);
									//Notification.show("Access denied");								
									//Notification.show("Access denied",Notification.TYPE_ERROR_MESSAGE); 
									empApprovalSub.setEnabled(false);

								} else {
									verticalLayout.removeAllComponents();
									if (empApprovalSub == null) {

										empApprovalSub = new EmpApprovalSub();

										empApprovalSub.getUserDetails(username, full_name, call_name);
										
										 
										// empApprovalSub.setUser(loggedInUser);

									}

									verticalLayout.addComponent(empApprovalSub);

								}
							}
							rs.close();
							st.close();
							con.close();
						} catch (Exception e) {
							// TODO: handle exception
						}

					} else if (treeMain.getValue().toString().equals("Board Approval")) {
						
						
						try {
							Connection con = JDBC_MY.con();
							Statement st = con.createStatement();
							String sql = "SELECT user_leval FROM  interv_user where user_name='" + username + "'";
							ResultSet rs = st.executeQuery(sql);
							while (rs.next()) {
								Integer ulevl = rs.getInt("user_leval");
								if ((ulevl>=11)&&(ulevl<=49)) {
									System.out.println(ulevl);
									//Notification.show("Access denied");
									//Notification.show("Access denied",Notification.TYPE_ERROR_MESSAGE);
									 
									empApprovalSub.setEnabled(false);

								} else {
									
									verticalLayout.removeAllComponents();
									 
									if (approvalViewSub == null) {

										approvalViewSub = new BoardApprovalViewSub();

										approvalViewSub.getUserDetails(username, full_name, call_name);
										 
										System.out.println(username + " test user");

									}
									 
									verticalLayout.addComponent(approvalViewSub);
									
								}
							}
							rs.close();
							st.close();
							con.close();
						} catch (Exception e) {
							// TODO: handle exception
						}

					

					}
					
					else if (treeMain.getValue().toString().equals("Reports")) {
						
						
						try {
							Connection con = JDBC_MY.con();
							Statement st = con.createStatement();
							String sql = "SELECT user_leval FROM  interv_user where user_name='" + username + "'";
							ResultSet rs = st.executeQuery(sql);
							while (rs.next()) {
								Integer ulevl = rs.getInt("user_leval");
								if ((ulevl>=11)&&(ulevl>=49)) {
									System.out.println("ffff"+ulevl);
									//Notification.show("Access denied");
									//Notification.show("Access denied",Notification.TYPE_ERROR_MESSAGE);
									
									//empreportsub = new EmpReportsSub();

									//empreportsub.getUserDetails(username, full_name, call_name);
									empreportsub.setEnabled(false);
									//verticalLayout.addComponent(empreportsub);

								} else {
									
									verticalLayout.removeAllComponents();
									 
									if (empreportsub == null) {

										empreportsub = new EmpReportsSub();

										empreportsub.getUserDetails(username, full_name, call_name);
										 
										System.out.println(username + " test user");

									}
									 
									verticalLayout.addComponent(empreportsub);
									
								}
							}
							rs.close();
							st.close();
							con.close();
						} catch (Exception e) {
							// TODO: handle exception
						}

					

					}
					
else if (treeMain.getValue().toString().equals("Promotions")) {
						
						
						try {
							Connection con = JDBC_MY.con();
							Statement st = con.createStatement();
							String sql = "SELECT user_leval FROM  interv_user where user_name='" + username + "'";
							ResultSet rs = st.executeQuery(sql);
							while (rs.next()) {
								Integer ulevl = rs.getInt("user_leval");
								if ((ulevl>=11)&&(ulevl>=49)) {
									System.out.println("ffff"+ulevl);
									//Notification.show("Access denied");
									//Notification.show("Access denied",Notification.TYPE_ERROR_MESSAGE);
									
									//empreportsub = new EmpReportsSub();

									//empreportsub.getUserDetails(username, full_name, call_name);
									empreportsub.setEnabled(false);
									//verticalLayout.addComponent(empreportsub);

								} else {
									
									verticalLayout.removeAllComponents();
									 
									if (promotionssub == null) {

										promotionssub = new PromotionsSub();

										promotionssub.getUserDetails(username, full_name, call_name);
										 
										System.out.println(username + " test user");

									}
									 
									verticalLayout.addComponent(promotionssub);
									
								}
							}
							rs.close();
							st.close();
							con.close();
						} catch (Exception e) {
							// TODO: handle exception
						}

					

					}

					else {

						verticalLayout.removeAllComponents();

					}

				}

			}
		});

		treeMain.expandItem("Interview");

		treeMain.setValue("New Employee");
//		treeMain.setValue("DashBoard");
		
		commandPasswordReset = new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {

				// verticalLayout.removeAllComponents();

				Window dd = new Window();

				if (userPasswordResetSub == null) {

					userPasswordResetSub = new UserPasswordResetSub();

					//userPasswordResetSub.setUserDetails(username);

				}

				UserPasswordResetSub us = new UserPasswordResetSub();
			    us.setUserDetails(username);
				dd.setContent(us);
				dd.center();
				dd.setWidth("450px");
				dd.setHeight("450px");
				dd.setResizable(false);
				dd.setModal(true);

				UI.getCurrent().addWindow(dd);

				// verticalLayout.addComponent(userPasswordResetSub);

			}
		};

		commandLogOut = new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {

				getSession().close();
				Page.getCurrent().open("http://172.20.10.16:8080/COOP_INTERVIEW/", null);

			}
		};

//		menuBar.addItem("Settings", FontAwesome.GEAR, commandSettings);
		MenuItem profile = menuBar.addItem("", FontAwesome.USER, null);
		profile.addItem("Change Password", commandPasswordReset);
		profile.addItem("Logout", commandLogOut);

	}

//	public void setTreeValue(String val) {
//
//		treeMain.setValue(val);
//
//	}

	Command commandPasswordReset;
	Command commandLogOut;

	private String username;

	private String full_name;

	private String call_name;
	private boolean isThreadActive;

	public DashBoardSub() {
//		com.vaadin.ui.JavaScript.getCurrent()
//				.execute("function closeListener() { catchClose(); } "
//						+ "window.addEventListener('beforeunload', closeListener); "
//						+ "window.addEventListener('unload', closeListener);");
//		com.vaadin.ui.JavaScript.getCurrent().addFunction("catchClose", arguments -> {
//			isThreadActive = false;
//			System.out.println("user has quit");
			if (username != null) {
				// loginLog("LOGOUT", username,
				// UI.getCurrent().getPage().getWebBrowser().getAddress());
			}

			// getSession().close();
//		});
		// runMsgSync();
		// TODO Auto-generated constructor stub

		treeMain.addItem("Interview");
		treeMain.setItemIcon("Motor", FontAwesome.CAR);
		 
//		treeMain.addItem("Dashboard");
//		treeMain.setParent("Dashboard", "Interview");
//		treeMain.setChildrenAllowed("Dashboard", false);
		
		treeMain.addItem("New Employee");
		treeMain.setParent("New Employee", "Interview");
		treeMain.setChildrenAllowed("New Employee", false);

		treeMain.addItem("HR Manager");
		treeMain.setParent("HR Manager", "Interview");
		treeMain.setChildrenAllowed("HR Manager", false);

		treeMain.addItem("Board Approval");
		treeMain.setParent("Board Approval", "Interview");
		treeMain.setChildrenAllowed("Board Approval", false);
		
		treeMain.addItem("Reports");
		treeMain.setParent("Reports", "Interview");
		treeMain.setChildrenAllowed("Reports", false);
		
//		treeMain.addItem("Promotions");
//		treeMain.setParent("Promotions", "Interview");
//		treeMain.setChildrenAllowed("Promotions", false);
		
		

//		treeMain.addItem("CEO");
//		treeMain.setParent("CEO", "Interview");
//		treeMain.setChildrenAllowed("CEO", false);
//		
//		treeMain.addItem("Director");
//		treeMain.setParent("Director", "Interview");
//		treeMain.setChildrenAllowed("Director", false);
//		
//		treeMain.addItem("Chairman");
//		treeMain.setParent("Chairman", "Interview");
//		treeMain.setChildrenAllowed("Chairman", false);

	}

	public void setFrame(Component component) {

		verticalLayout.removeAllComponents();
		verticalLayout.addComponent(component);

	}

	void loadUserDetails(UserResponseModel loggedInUser) {
		// TODO Auto-generated method stub
		try {

			File photo = new java.io.File(
					"\\\\172.20.10.20\\GENERAL\\pics\\" + loggedInUser.getUser().getUsername() + ".png");
			if (photo.exists()) {

				FileResource fr = new FileResource(photo);
				fr.setCacheTime(0);

				imageProfile.setSource(fr);

			} else {

				FileResource fr = new FileResource(new File("C:/APPS/app_icons/facebook.png"));
				fr.setCacheTime(0);

				imageProfile.setSource(fr);
			}
			labelUserName.setValue(full_name);
//			labelUserName.setValue(loggedInUser.getUser().getName());
//			labelUserBranch.setValue(loggedInUser.getBranch());

		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();

		}
	}

	void call_name(String username) {
		Connection co = null;
		Statement st = null;
		ResultSet re = null;
		try {
			co = JDBC_MY.con();
			st = co.createStatement();
			re = st.executeQuery("SELECT call_name FROM interv_user where user_name='" + username + "'");
			if (re.next()) {
				call_name = re.getString("call_name");
			} else {
//				Notification.show("User not Valid Access Denied",Notification.TYPE_ERROR_MESSAGE);
				System.out.println(call_name);
//				Page.getCurrent().open("http://localhost:8080/int-cn-system/", null);
			}
			re.close();
			st.close();
			co.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
