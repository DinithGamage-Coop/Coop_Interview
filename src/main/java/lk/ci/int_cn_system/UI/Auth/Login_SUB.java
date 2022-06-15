package lk.ci.int_cn_system.UI.Auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.vaadin.dialogs.ConfirmDialog;

import lk.ci.int_cn_system.Model.Auth.GenerateTokenModel;
import lk.ci.int_cn_system.Model.Auth.LoginSaveRequestModel;
import lk.ci.int_cn_system.Model.Auth.UserResponseModel;
//import lk.ci.int_cn_system.Model.Setting.AddressTypeModel;
//import lk.ci.int_cn_system.Model.Setting.CityModel;
//import lk.ci.int_cn_system.Model.Setting.DepartmentModel;
//import lk.ci.int_cn_system.Model.Setting.DistrictModel;
import lk.ci.int_cn_system.UI.DashBoardSub;
//import lk.ci.int_cn_system.UI.Setting.MaritalStatusSub;
import lk.ci.int_cn_system.Utils.ConstantData;
import lk.ci.int_cn_system.Utils.JDBC_COOP;
import lk.ci.int_cn_system.Utils.MD5;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class Login_SUB extends LoginForm {

	//private String user;
	private int level;
	private String branch;
	private Window win;
	private String FullName;
	private DashBoardSub mainUI;

	private List<GenerateTokenModel> generateTokenModel;
	private List<LoginSaveRequestModel> loginSaveRequestModel;
	private List<UserResponseModel> userResponseModel;

	private String token;
	private String user;

	public void getToken(String token) {
		this.token = token;
	}

//	private MainUI_SUB mainUI;

	public Login_SUB() {
		Page.getCurrent().setTitle("COOP INTERVIEWS");
		button_login.setClickShortcut(KeyCode.ENTER);

		button_login.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (textField_USERNAME.getValue().isEmpty()) {
					Notification.show("Username Is Empty", Notification.TYPE_ERROR_MESSAGE);
				} else if (passwordField.getValue().isEmpty()) {
					Notification.show("Password Is Empty", Notification.TYPE_ERROR_MESSAGE);
				}

				else {
					login();

				}

			}
		});
		
//		button_forgot_password.addClickListener(new Button.ClickListener() {
//
//			@Override
//			public void buttonClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				if(textField_USERNAME.getValue().trim().isEmpty()) {
//					
//					Notification.show("Enter the username !");
//					return;
//				}
//
//				ConfirmDialog.show(getUI(), "Confirmation", "Send password reset verification e-mail ?", "Continue", "Cancel",
//						new ConfirmDialog.Listener() {
//							public void onClose(ConfirmDialog dialog) {
//								if (dialog.isConfirmed()) {
//								 
//									int responseCode = 0;
//									try {
//										 
//										Gson gson = new Gson();
//										HttpClient client = HttpClientBuilder.create().build();
//										HttpGet request = new HttpGet(ConstantData.baseUrl+"users/PasswordResetRequest/"+textField_USERNAME.getValue().trim());
//										request.setHeader("Accept", "application/json");
//										request.setHeader("Content-Type", "application/json");
//  
//										HttpResponse response = client.execute(request);
//										InputStream inputStream = (InputStream) response.getEntity().getContent();
//										BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//										String inputLine;
//										String getRes = "";
//										while ((inputLine = in.readLine()) != null) {
//											getRes = getRes + inputLine + "\n";
//										}
//										in.close();
//										
//
//										responseCode = response.getStatusLine().getStatusCode();
//										if (responseCode == 200) {
//											
//											 String email=getRes.trim();
//											 
//											 PasswordResetSub lsb=new PasswordResetSub();
//												Window ww = new Window();
//
//												ww.center();
//												//ww.setClosable(true);
//												ww.setModal(true);
//												ww.setResponsive(true);
//
//												ww.setResizable(false);
//												ww.setWidth("445px");
//												ww.setHeight("440px");
//
//												lsb.getDetails(ww,textField_USERNAME.getValue().trim(),email);
//												ww.setContent(lsb);
//												UI.getCurrent().addWindow(ww);
//
//										} else if (responseCode == 400) {
//											Notification.show("Error", "Validation Error", Notification.TYPE_ERROR_MESSAGE);
//										} else if (responseCode == 404) {
//											Notification.show("Error", "No records Found", Notification.TYPE_ERROR_MESSAGE);
//										} else if (responseCode == 401) {
//											Notification.show("Error", "Unauthorized", Notification.TYPE_ERROR_MESSAGE);
//										} else if (responseCode == 500) {
//											Notification.show("Error", "Server Error", Notification.TYPE_ERROR_MESSAGE);
//										} else {
//											Notification.show("Error", Integer.toString(responseCode), Notification.TYPE_ERROR_MESSAGE);
//										}
//		 
//									} catch (Exception e) {
//										e.printStackTrace();
//										Notification.show(e.toString(), Notification.Type.ERROR_MESSAGE);
//									}
//								} else {
//									// User did not confirm
//								}
//							}
//						});
//			}
//		});
	}

	void login() {

		try {
			String pwd = new String(passwordField.getValue()).toUpperCase();
			String un = textField_USERNAME.getValue().toUpperCase();
			if ((!textField_USERNAME.getValue().isEmpty()) && (!pwd.isEmpty())) {

				GenerateTokenModel gt = new GenerateTokenModel();
				LoginSaveRequestModel lr = new LoginSaveRequestModel();

				gt.setUsername(textField_USERNAME.getValue().trim());
				gt.setPassword(passwordField.getValue().trim());

				lr.setIpAddress(UI.getCurrent().getPage().getWebBrowser().getAddress());
				lr.setLoginType("LOGIN");
				lr.setUserName(textField_USERNAME.getValue().trim());

				gt.setLoginSaveRequest(lr);

				int responseCode = 0;

				
					// new my code - chathura - Start
					// Get request
			/*		Gson gson = new Gson();
					HttpClient client = HttpClientBuilder.create().build();
					HttpPost request = new HttpPost(ConstantData.baseUrl+"users/authenticate");
					request.setHeader("Accept", "application/json");
					request.setHeader("Content-Type", "application/json");

					// only for post & Put
					StringEntity body = new StringEntity(gson.toJson(gt));
					request.setEntity(body);

					HttpResponse response = client.execute(request);
					InputStream inputStream = (InputStream) response.getEntity().getContent();
					BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
					String inputLine;
					String getRes = "";
					while ((inputLine = in.readLine()) != null) {
						getRes = getRes + inputLine + "\n";
					}
					in.close();
					

					responseCode = response.getStatusLine().getStatusCode();
					if (responseCode == 200) {
						
						UserResponseModel m = new UserResponseModel();
						m = gson.fromJson(getRes, UserResponseModel.class);						
						
						DashBoardSub dashSub = new DashBoardSub();
						dashSub.setLoggedInUser(m);					

						mainUI = new DashBoardSub();
						mainUI.setLoggedInUser(m);
						UI.getCurrent().setContent(mainUI);
						win.close();

					} else if (responseCode == 400) {
						Notification.show("Error", "Validation Error", Notification.TYPE_ERROR_MESSAGE);
					} else if (responseCode == 404) {
						Notification.show("Error", "No records Found", Notification.TYPE_ERROR_MESSAGE);
					} else if (responseCode == 401) {
						Notification.show("Error", "Unauthorized", Notification.TYPE_ERROR_MESSAGE);
					} else if (responseCode == 500) {
						Notification.show("Error", "Server Error", Notification.TYPE_ERROR_MESSAGE);
					} else {
						Notification.show("Error", Integer.toString(responseCode), Notification.TYPE_ERROR_MESSAGE);
					}*/
					//// new my code -chathura - End

					// sira start
					
					Connection co = JDBC_COOP.con();
					Statement st = co.createStatement();

					ResultSet r = st.executeQuery(
							"select SFC_CODE,USERNAME,FULL_NAME,PASSWORD,SFC_BRN_CODE,SFC_LEVEL,SLC_BRN_DESC,DIVISION,USER_ACTIVE from user_login where USERNAME='"
									+ un + "'");

					if (r.next()) {
						String act = r.getString("USER_ACTIVE");
						// System.out.println(pwd);
						// System.out.println(act);
						if (r.getString("PASSWORD").equals(MD5.cryptWithMD5(pwd)) && act.equals("Y")) {
						if ((!textField_USERNAME.getValue().isEmpty()) && (!pwd.isEmpty())) {
							/// LOGIN
							// Notification.show("Login succcessfull !");

							branch = r.getString("SLC_BRN_DESC");
							FullName = r.getString("FULL_NAME");

							level = Integer.parseInt(r.getString("SFC_LEVEL"));

							user = un;
							String browser = "";
							WebBrowser wb = Page.getCurrent().getWebBrowser();
							if (wb.isIE()) {
								browser = "I.E.";
							} else if (wb.isChrome()) {
								browser = "Chrome";
							} else if (wb.isFirefox()) {
								browser = "Firefox";
							} else if (wb.isAndroid()) {
								browser = "Android";
							} else if (wb.isEdge()) {
								browser = "MS Edge";
							} else if (wb.isOpera()) {
								browser = "Opera";
							}

							Page.getCurrent().setTitle(user + "@ Co-operative Insurance");
							VaadinService.getCurrentRequest().getWrappedSession().setAttribute("userName",
									user + "@" + branch.replaceAll(" ", "_") + "/" + browser);
							///////////////////////
							// deleteTEMP();

							FileResource fr = null;
							java.io.File photo = new java.io.File("C:\\APPS\\profile_pics\\" + un + ".png");
						if (photo.exists()) {

								fr = new FileResource(photo);
								fr.setCacheTime(0);

							}

						/*	User user = new User();
							user.setCode(r.getString("SFC_CODE"));
							user.setUsername(r.getString("USERNAME"));
							user.setFullName(r.getString("FULL_NAME"));
							user.setLevel(r.getInt("SFC_LEVEL"));
							user.setBranch(r.getString("SLC_BRN_DESC"));*/
							mainUI = new DashBoardSub();
							mainUI.getUserDetails(user,FullName,branch,level);
							UI.getCurrent().setContent(mainUI);

							win.close();

							// sira end

						} else {
							System.out.println("err 1");

							Notification.show("Invalid username or password !", Notification.Type.ERROR_MESSAGE);
						}

					} else {
						System.out.println("err 2");
						Notification.show("Invalid username or password !", Notification.Type.ERROR_MESSAGE);
					}
					r.close();
					st.close();
					co.close();
			

			} else {
				Notification.show("username or password is incorrect !", Notification.Type.WARNING_MESSAGE);
			}
					
					
			}		
					
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
	}

	public void getWindow(Window w) {

		win = w;
		// mainUI = mUI;
	}

//	void deleteTEMP() {
//
//		try {
//
//			File temp = new File("C:\\APPS\\tmp_img\\");
//			Calendar cnow = Calendar.getInstance();
//			cnow.add(Calendar.MINUTE, -15);
//
//			Date now = cnow.getTime();
//
//			for (File f : temp.listFiles()) {
//				if (FileUtils.isFileOlder(f, now)) {
//					FileUtils.forceDelete(f);
//				}
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
}
