package lk.ci.int_cn_system.UI.Auth;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.security.auth.Refreshable;
import javax.swing.JOptionPane;
import javax.xml.soap.Text;

import org.apache.commons.io.FileUtils;
import org.jsoup.select.Evaluator.IsEmpty;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import lk.ci.int_cn_system.Utils.ConstantData;
import lk.ci.int_cn_system.Utils.JDBC_COOP;
import lk.ci.int_cn_system.Utils.JDBC_MY;
import lk.ci.int_cn_system.Utils.SysPath;

public class EmpApprovalSub extends EmpApproval {

	UI thisUI = UI.getCurrent().getUI();
	private String username;
	FileResource fr = new FileResource(new File("\\\\172.20.10.20\\general\\dist\\icons\\19_2.png"));
	FileResource fr2 = new FileResource(new File("\\\\172.20.10.20\\general\\dist\\icons\\1_!.png"));
	private String call_Name;
	private String p_confirmation;
	protected boolean isThreadActive;
	private String cv_no_temp;
	private HashMap<String, File> hm_img;
	Tree tree_4tos;

	public EmpApprovalSub() {

		comboBox_department.setValue("SELECT");

		load_cv_number();
		designation_load();

		button_reject.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (comboBox_Cv_Nm.isEmpty()) {
					Notification.show("Select the cv number", Notification.TYPE_WARNING_MESSAGE);
				}

				else {
					approval_reject();
					clearFields();
				}

			}
		});

		button_add_designation.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				MySub sub = new MySub();
				UI.getCurrent().addWindow(sub);
				designation_load();

			}
		});

		comboBox_Cv_Nm.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

				setdata();
				load_approval();

				checkBox_hRM.setEnabled(true);
				checkBox_hRM.setIcon(null);
				checkBox_hRM.setCaption("HR - MANAGER");

			}
		});

		comboBox_branch.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				depBybranch();
			}
		});

		comboBox_designation.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				button_load_designation.addClickListener(new Button.ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub

						designation_load();

					}
				});
			}
		});

		button_load_designation.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				designation_load();

			}
		});

		button_date_cal.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (dateField_startdte.getValue() == null) {
					Notification.show("Plaese Select the Effective Date", Notification.TYPE_WARNING_MESSAGE);
				} else if (textField_montscunt.getValue() == null) {
					Notification.show("Please Select the Months Count", Notification.TYPE_WARNING_MESSAGE);
				}

				else {
					Date_Calculator();

				}

			}

		});

		button_board_membr.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (textField_board_membr.isEmpty()) {
					Notification.show("Enter Number of Members", Type.ERROR_MESSAGE.WARNING_MESSAGE);
					load_cv_number();
				}

				else {

					board_mem_count();

				}

			}
		});

		button_accept.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				validtion_save();
			}
		});

		checkBox_hRM.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					System.out.println(cv_no_temp);
					st.executeUpdate("update cv_approval set hr_app='" + call_Name + "',hr_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();

				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		button_cv_view.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (comboBox_Cv_Nm.isEmpty()) {
					Notification.show("Select the CV Number", Notification.TYPE_WARNING_MESSAGE);
				}

				else {

					try {

						String ffff = ("\\\\172.20.10.14\\HR_File\\coop_interview_cv\\"
								+ comboBox_Cv_Nm.getValue().toString().trim() + ".pdf");

						// File f = (File) hm_img.get(tree_4tos.getValue().toString());

						if (ffff.equals(null)) {
							Notification.show("No cv", Notification.TYPE_WARNING_MESSAGE);
						}

						else {

							File f = new File(ffff);

							if (!f.exists()) {

								Notification.show("CV not uploded", Notification.TYPE_ERROR_MESSAGE);
							} else {

//						File pdf = new File(System.getProperty(ffff);

//						File pdf = new File(System.getProperty("user.home" + "/UW/temp/" + savedQuoNo + "-"
//								+ System.currentTimeMillis() + ".pdf";
								//
//						FileUtils.copyURLToFile(new URL("\\\\172.20.10.14\\HR_File\\coop_interview_cv\\"+comboBox_Cv_Nm.getValue().toString().trim() + ".pdf"), pdf);
								//
//						FileResource fr2 = new FileResource(pdf);
								//
//						getUI().getPage().open(fr2, "_blank", true);

								// -- File folTEMP3 = new File(SysPath.GetSysPath() + "tmp_hr_img/");

								// - FileUtils.copyFileToDirectory(new File(f.getPath()), folTEMP3);
								FileResource fr3 = new FileResource(f);
								System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + fr3);
								getUI().getPage().open(fr3, "_blank", true);

//						Window expEST = new Window(f.getName());
//						expEST.setWidth("100%");
//						expEST.setHeight("100%");
//						expEST.center();
								//
//						UI.getCurrent().addWindow(expEST);
//						VerticalLayout vll = new VerticalLayout();
//						expEST.setContent(vll);
//						int ch = 0; 
//						File folTEMP = new File(SysPath.GetSysPath() + "tmp_hr_img/");
//						
//						
//						FileUtils.copyFileToDirectory(new File(f.getPath()), folTEMP);
//						FileResource fr2 = new FileResource(folTEMP);
//						System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+fr2);
//						getUI().getPage().open(fr2, "_blank", true);
								//
								//
//						DemoUI dui = new DemoUI();
//						vll.addComponent(dui);
//						Path pth = new File(f.getPath()).toPath();
//						String cr_date = "";
//						File copF = new File(folTEMP, f.getName());
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
//						try {
//							BasicFileAttributes attributes = Files.readAttributes(pth, BasicFileAttributes.class);
//							long c_date = attributes.creationTime().toMillis();
//							Date cre_d = new Date(c_date);
//							cr_date = sdf.format(cre_d);
//							FileUtils.copyFileToDirectory(new File(f.getPath()), folTEMP);
//							
//							
								//
//						} catch (Exception e) {
//							// TODO: handle exception
//							e.printStackTrace();
//						}

								// (FileUtils.readFileToString(new File(fr,f.getName().replaceAll(".jpg",
								// ".txt")))).replaceAll("\n", "<br>").replaceAll("null", "-")
								/*
								 * String tagD = ""; if (new File(fr.getPath(), f.getName().replaceAll(".jpg",
								 * ".txt")).exists()) { tagD = (FileUtils .readFileToString(new
								 * File(fr.getPath(), f.getName().replaceAll(".jpg", ".txt"))))
								 * .replaceAll("\n", "<br>").replaceAll("null", "-"); }
								 */

//						dui.set4TO(FileUtils.readFileToByteArray(copF), "<font color='black'>" + f.getName()
//								+ "</font><br>Uploaded Date : " + cr_date + "<br>");//

							}
						}

					} catch (Exception e) {
						// TODO: handle exception
						// e.printStackTrace();

						Notification.show("No cv here", Notification.TYPE_WARNING_MESSAGE);
					}

				}

			}
		});

	}

	void setdata() {

		if (comboBox_Cv_Nm.isEmpty()) {

			clearFields();
		}

		else {

			try {
				Connection con = JDBC_MY.con();
				Statement st = con.createStatement();
				String sql = "SELECT * FROM cv_create where cv_no='" + comboBox_Cv_Nm.getValue() + "'  ";
				ResultSet rs = st.executeQuery(sql);
				while (rs.next()) {
					String s1 = rs.getString("cv_no");
//					String s2 = rs.getString("full_name");
					String s3 = rs.getString("name_with_instials");
					String s4 = rs.getString("nic");
					String s5 = rs.getString("gender");
					String s6 = rs.getString("tp_no");
					String s7 = rs.getString("e_mail");
					String s8 = rs.getString("address");

					double dbl = Double.parseDouble(rs.getString("salary"));
//					NumberFormat formatter = new DecimalFormat("#0.00");     
					String s9 = String.format("රු. %,.2f", dbl);

					String s10 = rs.getString("company");
					String s11 = rs.getString("highest_qualification");
					String s12 = rs.getString("requ_possition");
					Date s16 = rs.getDate("dob");
					String s17 = rs.getString("civil_state");
					String s18 = rs.getString("full_name");
					String s19 = rs.getString("emp_mode");
					String s22 = rs.getString("region");

					cv_no_temp = s1;

					label_nameWithInitials.setValue("<font color='#2932F1'><b>" + s3 + "</b></font>");
					label_nIC.setValue("<font color='#2932F1'><b>" + s4 + "</b></font>");
					label_gender.setValue("<font color='#2932F1'><b>" + s5 + "</b></font>");
					label_telephoneNo.setValue("<font color='#2932F1'><b>" + s6 + "</b></font>");
//					label_email.setValue("<font color='#2932F1'><b>" + s7 + "</b></font>");
					label_address.setValue("<font color='#2932F1'><b>" + s8 + "</b></font>");
					label_reqsalary.setValue("<font color='#2932F1'><b>" + s9 + "</b></font>");
					label_company.setValue("<font color='#2932F1'><b>" + s10 + "</b></font>");
					label_highestQualification.setValue("<font color='#2932F1'><b>" + s11 + "</b></font>");
					label_requestingPossition.setValue("<font color='#2932F1'><b>" + s12 + "</b></font>");

					SimpleDateFormat dab = new SimpleDateFormat("yyyy-MM-dd");

					label_dateofbirth.setValue("<font color='#2932F1'><b>" + dab.format(s16) + "</b></font>");
					label_civil_state.setValue("<font color='#2932F1'><b>" + s17 + "</b></font>");
					label_fullname.setValue("<font color='#2932F1'><b>" + s18 + "</b></font>");
					label_emp_mode.setValue("<font color='#2932F1'><b>" + s19 + "</b></font>");
					label_region.setValue("<font color='#2932F1'><b>" + s22 + "</b></font>");
					comboBox_department.setValue("Select");
					button_accept.setEnabled(true);
					button_reject.setEnabled(true);

				}

				rs.close();
				st.close();
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	void load_cv_number() {
		try {
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			comboBox_Cv_Nm.removeAllItems();
			ResultSet rs = st.executeQuery("SELECT cv_no FROM cv_create where status='0'");
			while (rs.next()) {
				String rsv = rs.getString("cv_no");
				comboBox_Cv_Nm.addItem(rsv);
			}
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	void Date_Calculator() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int d = Integer.parseInt(textField_montscunt.getValue().toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateField_startdte.getValue());
		cal.add(Calendar.MONTH, d);
		Date dd = cal.getTime();

		label_Position_Confirmation.setValue("<font color='#2932F1'><b>" + df.format(dd) + "</b></font>");
		p_confirmation = df.format(dd);

	}

	void validtion_save() {
		if (textField_offed_salary.getValue().equals("")) {
			Notification.show("Please Fill The Offered Salary", Notification.TYPE_WARNING_MESSAGE);
		} else if (textField_allwnce_1.getValue().equals("")) {
			Notification.show("Please Fill The Allowances", Notification.TYPE_WARNING_MESSAGE);
		} else if (textField_allwnce_2.getValue().equals("")) {
			Notification.show("Please Fill The Travelling", Notification.TYPE_WARNING_MESSAGE);
		} else if (comboBox_branch.getValue() == null) {
			Notification.show("Please Select The Branch", Notification.TYPE_WARNING_MESSAGE);
		} else if (combobox_emptype.getValue() == null) {
			Notification.show("Please Select The Employee Type", Notification.TYPE_WARNING_MESSAGE);
		} else if (dateField_startdte.getValue() == null) {
			Notification.show("Please Select The Date", Notification.TYPE_WARNING_MESSAGE);
		} else if (textField_montscunt.getValue() == null) {
			Notification.show("Please Select The Months Count", Notification.TYPE_WARNING_MESSAGE);
		} else if (comboBox_designation.getValue() == null) {
			Notification.show("Please Select The Designation", Notification.TYPE_WARNING_MESSAGE);
		} else {
			approval_save();
			checkBox_hRM.setValue(true);
			checkbox_click();

		}
	}

	void approval_save() {

		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat db = new SimpleDateFormat("yyyy-MM-dd");
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			String query = "insert into cv_approval(cv_no, designation,barnch,department,offered_salary, user_name, emp_type, allownce_1, allownce_2, other_benifits, conditions, effective_date, months_count, position_confirmation, create_by, created_date,monthly_target,period) values('"
					+ comboBox_Cv_Nm.getValue() + "','" + comboBox_designation.getValue().toString().toUpperCase()
					+ "','" + comboBox_branch.getValue().toString().toUpperCase() + "','"
					+ comboBox_department.getValue().toString() + "','" + textField_offed_salary.getValue() + "','"
					+ username + "','" + combobox_emptype.getValue().toString().toUpperCase() + "','"
					+ textField_allwnce_1.getValue() + "','" + textField_allwnce_2.getValue() + "','"
					+ text_area_otherBenefits.getValue().toUpperCase() + "','"
					+ text_area_conditions.getValue().toUpperCase() + "','" + db.format(dateField_startdte.getValue())
					+ "','" + textField_montscunt.getValue() + "','" + p_confirmation + "','" + username + "','"
					+ df.format(new Date()) + "','" + textField_monthlyTarget.getValue() + "','"
					+ textField_period.getValue() + "')";
			st.executeUpdate(query);
			cv_no_temp = comboBox_Cv_Nm.getValue().toString();
			st.executeUpdate("update cv_create set status='1' where cv_no='" + comboBox_Cv_Nm.getValue() + "'");
			button_accept.setEnabled(false);
			button_reject.setEnabled(false);
			Notification.show("Successfully Saved");
			comboBox_department.setEnabled(true);
			comboBox_department.clear();
			clearFields();

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	void load_approval() {

		try {
			Connection co = JDBC_MY.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery("SELECT * FROM cv_approval where cv_no='" + cv_no_temp + "'");
			if (re.next()) {
				if (re.getString("hr_app") != null) {
					checkBox_hRM.setValue(false);
					checkBox_hRM.setIcon(fr);
					checkBox_hRM.setCaption(re.getString("hr_app"));
					checkBox_hRM.setEnabled(false);
				} else {
					checkBox_hRM.setValue(false);
					checkBox_hRM.setIcon(fr2);

				}

			} else {
				checkBox_hRM.setValue(false);
				checkBox_hRM.setIcon(fr2);
			}

		} catch (Exception e) {

			e.printStackTrace();
			// TODO: handle exception
		}
	}

	void board_mem_count() {

		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat db = new SimpleDateFormat("yyyy-MM-dd");
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			String query = "UPDATE board_member_count SET board_member_count='" + textField_board_membr.getValue()
					+ "',create_by='" + username + "',created_date='" + df.format(new Date()) + "' WHERE id=1";

			st.executeUpdate(query);
//			cv_no_temp = comboBox_Cv_Nm.getValue().toString();
//			st.executeUpdate ("insert into board_member_count(create_by, created_date) values('" + username + "','"+ df.format(new Date()) + "'");
//			button_accept.setEnabled(false);
			textField_board_membr.clear();
			Notification.show("Successfully Updated");
//			comboBox_department.setEnabled(true);
//			comboBox_department.clear();
//			clearFields();

		}

		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	void load_approval_enter_data() {
		try {
			Connection co = JDBC_MY.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery(
					"select * from cv_approval app inner join cv_create crt on app.cv_no=crt.cv_no where crt.status='1'");
			if (re.next()) {
				label_nameWithInitials.setValue(re.getString("name_with_instials"));
				label_address.setValue(re.getString("address"));
				label_civil_state.setValue(re.getString("civil_state"));
				label_company.setValue(re.getString("company"));
				label_dateofbirth.setValue(re.getString("dob"));
//				label_email.setValue(re.getString("e_mail"));
				label_fullname.setValue(re.getString("full_name"));
				label_emp_mode.setValue(re.getString("emp_mode"));
				label_gender.setValue(re.getString("gender"));
				label_highestQualification.setValue(re.getString("highest_qualification"));
				label_nIC.setValue(re.getString("nic"));
				label_Position_Confirmation.setValue(re.getString("position_confirmation"));
				label_region.setValue(re.getString("region"));
				label_reqsalary.setValue(re.getString("salary"));
				label_requestingPossition.setValue(re.getString("requ_possition"));
				label_telephoneNo.setValue(re.getString("tp_no"));

				comboBox_branch.setValue(re.getString("branch"));
				comboBox_Cv_Nm.setValue(re.getString("cv_no"));
				comboBox_department.setValue(re.getString("department"));
				combobox_emptype.setValue(re.getString("emp_mode"));

				textField_allwnce_1.setValue(re.getString("allownce_1"));
				textField_allwnce_2.setValue(re.getString("allownce_2"));
				comboBox_designation.setValue(re.getString("designation"));
				textField_montscunt.setValue(re.getString("months_count"));
				textField_offed_salary.setValue(re.getString("offered_salary"));
				textField_monthlyTarget.setValue(re.getString("monthly_target"));
				textField_period.setValue(re.getString("period"));

				text_area_otherBenefits.setValue(re.getString("other_benifits"));
				text_area_conditions.setValue(re.getString("conditions"));

			}

			re.close();
			st.close();
			co.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	void branch_load() {
		try {
			Connection co = JDBC_COOP.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery("SELECT * FROM general_branches");
			// ResultSet re = st.executeQuery("SELECT * FROM SM_M_SALESLOC");
			while (re.next()) {
				comboBox_branch.addItem(re.getString("SLC_BRN_DESC"));
			}
			re.close();
			st.close();
			co.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	void depBybranch() {
		if (comboBox_branch.getValue().toString().equals("HEAD OFFICE")) {
			comboBox_department.setEnabled(true);
			department_load();

		} else {

			comboBox_department.setValue("SELECT");
			comboBox_department.setEnabled(false);

		}
	}

	void approval_reject() {

		try {
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			cv_no_temp = comboBox_Cv_Nm.getValue().toString();
			st.executeUpdate("update cv_create set status='8' where cv_no='" + comboBox_Cv_Nm.getValue() + "'");
			Notification.show("Successfully Reject");
			load_cv_number();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	void department_load() {
		try {
			Connection co = JDBC_MY.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery("SELECT * FROM e_details.departments");
			while (re.next()) {

				comboBox_department.addItem(re.getString("dpt_name"));

			}
			re.close();
			st.close();
			co.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	private void clearFields() {

		label_civil_state.setValue("");
		label_company.setValue("");
		label_nameWithInitials.setValue("");
		label_telephoneNo.setValue("");
		label_address.setValue("");
//		label_email.setValue("");
		label_fullname.setValue("");
		label_gender.setValue("");
		label_emp_mode.setValue("");
		label_nIC.setValue("");
		label_dateofbirth.setValue("");
		label_region.setValue("");
		label_highestQualification.setValue("");
		label_dateofbirth.setValue("");
		label_requestingPossition.setValue("");
		label_reqsalary.setValue("");
		textField_offed_salary.setValue("");
		textField_monthlyTarget.setValue("");
		textField_period.setValue("");
		textField_allwnce_1.setValue("");
		textField_allwnce_2.setValue("");
		comboBox_designation.clear();
		textField_montscunt.clear();
		label_Position_Confirmation.setValue("");
		text_area_otherBenefits.setValue("");
		text_area_conditions.setValue("");
		comboBox_department.clear();
		combobox_emptype.clear();
		dateField_startdte.clear();
		comboBox_branch.setValue("");
		load_cv_number();
		placeholder();
	}

	void designation_load() {
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

	void placeholder() {
		textField_allwnce_1.setInputPrompt("0.00");
		textField_allwnce_2.setInputPrompt("0.00");
	}

	void checkbox_click() {
		checkBox_hRM.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					System.out.println(cv_no_temp);
					st.executeUpdate("update cv_approval set hr_app='" + call_Name + "',hr_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();

				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});
	}

	public void getUserDetails(String user, String fullname, String call) {
		// TODO Auto-generated method stub
		username = user;
		call_Name = call;
		clearFields();
		load_cv_number();
		branch_load();
		department_load();

	}

}
