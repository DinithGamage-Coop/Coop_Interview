package lk.ci.int_cn_system.UI.Auth;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.Refreshable;

import org.apache.commons.io.FileUtils;
import org.jsoup.select.Evaluator.IsEmpty;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import lk.ci.int_cn_system.Utils.JDBC_COOP;
import lk.ci.int_cn_system.Utils.JDBC_MY;

public class BoardApprovalViewSub extends BoardApprovalView {

	UI thisUI = UI.getCurrent().getUI();
	private String username;
	private String full_name;
	FileResource fr = new FileResource(new File("\\\\172.20.10.20\\general\\dist\\icons\\19_2.png"));
	FileResource fr2 = new FileResource(new File("\\\\172.20.10.20\\general\\dist\\icons\\1_!.png"));
	private String call_Name;
	protected boolean isThreadActive;
	private String cv_no_temp;

	public BoardApprovalViewSub() {

		viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");

		load_cv_number();
		load_approval();
//		comboBox_Cv_Nm.addValueChangeListener(new ValueChangeListener() {
//
//			@Override
//			public void valueChange(ValueChangeEvent event) {
//				// TODO Auto-generated method stub
//				setdata();
//			}
//		});

		button_date_cal.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				int d = Integer.parseInt(textField_montscunt.getValue());
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateField_startdte.getValue());
				cal.add(Calendar.MONTH, d);
				Date dd = cal.getTime();
				label_Position_Confirmation.setValue(df.format(dd));
			}

		});

		btn_search.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				clear_All_Fields();
				load_approval();
				load_cv_number();

				try {

					Connection con = JDBC_MY.con();
					Statement st = con.createStatement();

					// String sql = "SELECT * FROM cv_create where cv_no='" +
					// comboBox_Cv_Nm.getValue() + "' ";
					String sql = "SELECT * from cv_approval app inner join cv_create crt on app.cv_no=crt.cv_no where crt.status=1";
					ResultSet rs = st.executeQuery(sql);
					if (!rs.next()) {
						viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");

						// Notification.show("hari");
					} else {
						load_cv_number();
						setdata();
						load_approval();
						// viewstatus.setValue("CV Pending Click Here >>>");
					}
//					while (rs.next()) {
//						
//						String s1 = rs.getString("cv_no");
//						System.out.println("44444"+s1);
//						
//						if (s1.equals(null)) {
//							System.out.println("hari");
//							Notification.show("hari");
//						}
//						else {
//							System.out.println("waradi");
//							Notification.show("waradi");
//						}

					rs.close();
					st.close();
					con.close();

				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

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
					st.executeUpdate("update cv_approval set hr_app='" + call_Name + "',hr_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					approval_done();
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_director.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set director_app='" + call_Name + "',director_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();

					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_chairman.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set chairman_app='" + call_Name + "',chairman_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();
					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_mD.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set md_app='" + call_Name + "',md_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();
					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_cEO.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set ceo_app='" + call_Name + "',ceo_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();
					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_gM.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set gm_app='" + call_Name + "',gm_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();
					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_gmtechnical.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set gm_tech_app='" + call_Name + "',gm_tech_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();
					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

		checkBox_hOD.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				try {
					SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Connection co = JDBC_MY.con();
					Statement st = co.createStatement();
					st.executeUpdate("update cv_approval set hod_app='" + call_Name + "',hod_date='"
							+ dat.format(new Date()) + "' where cv_no='" + cv_no_temp + "'");
					load_approval();
					// load_approval_enter_data();
					approval_done();
					clearFields();
					viewstatus.setValue("<font color='red'><b>" + "CV Pending >>>" + "</b></font>");
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

			}

		});

	}

	void setdata() {

		try {

			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();

			// String sql = "SELECT * FROM cv_create where cv_no='" +
			// comboBox_Cv_Nm.getValue() + "' ";
			String sql = "SELECT * from cv_approval app inner join cv_create crt on app.cv_no=crt.cv_no where crt.status=1 order by app.cv_no desc";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next()) {

				System.out.println(rs);

				String s1 = rs.getString("cv_no");
				String s2 = rs.getString("full_name");
				String s3 = rs.getString("name_with_instials");
				String s4 = rs.getString("nic");
				String s5 = rs.getString("gender");
				String s6 = rs.getString("tp_no");
				String s7 = rs.getString("e_mail");
				String s8 = rs.getString("address");

				double dbl = Double.parseDouble(rs.getString("salary"));
//				NumberFormat formatter = new DecimalFormat("#0.00");     
				String s9 = String.format("රු. %,.2f", dbl);
//				String s9 = rs.getString("salary");

				String s10 = rs.getString("company");
				String s11 = rs.getString("highest_qualification");
				String s12 = rs.getString("requ_possition");
//				String s13 = rs.getString("emp_category");
				String s14 = rs.getString("offered_salary");
//				String s15 = rs.getString("titel");
				Date s16 = rs.getDate("dob");
				String s17 = rs.getString("civil_state");
//				String s18 = rs.getString("land_line");
				String s19 = rs.getString("emp_mode");
				String s20 = rs.getString("barnch");
				String s21 = rs.getString("department");

				String s22 = rs.getString("region");
				String s23 = rs.getString("allownce_1");
				String s24 = rs.getString("allownce_2");
				String s25 = rs.getString("months_count");
				String s26 = rs.getString("position_confirmation");
				String s27 = rs.getString("other_benifits");
				String s28 = rs.getString("conditions");
				String s29 = rs.getString("designation");
				String s30 = rs.getString("emp_type");

//				textField_cvno.setValue(s1);
				viewstatus.setValue("<font color='#2932F1'><b>" + s1 + "</b></font>");
//				fullnm.setValue(s2);
				label_nameWithInitials.setValue("<font color='#2932F1'><b>" + s3 + "</b></font>");
				label_nIC.setValue("<font color='#2932F1'><b>" + s4 + "</b></font>");
				label_gender.setValue("<font color='#2932F1'><b>" + s5 + "</b></font>");
				label_telephoneNo.setValue("<font color='#2932F1'><b>" + s6 + "</b></font>");
				label_email.setValue("<font color='#2932F1'><b>" + s7 + "</b></font>");
				label_address.setValue("<font color='#2932F1'><b>" + s8 + "</b></font>");
				label_reqsalary.setValue("<font color='#2932F1'><b>" + s9 + "</b></font>");
				label_company.setValue("<font color='#2932F1'><b>" + s10 + "</b></font>");
				label_highestQualification.setValue("<font color='#2932F1'><b>" + s11 + "</b></font>");
				label_requestingPossition.setValue("<font color='#2932F1'><b>" + s12 + "</b></font>");
//				comboBox_empCategory.setValue(s13);
				textField_offed_salary.setValue(s14);
//				comboBox_titel.setValue(s15);

				SimpleDateFormat dab = new SimpleDateFormat("yyyy-MM-dd");

				label_dateofbirth.setValue("<font color='#2932F1'><b>" + dab.format(s16) + "</b></font>");
				label_civil_state.setValue("<font color='#2932F1'><b>" + s17 + "</b></font>");
//				textField_landline.setValue(s18);
				label_emp_mode.setValue("<font color='#2932F1'><b>" + s19 + "</b></font>");
//				textField_branch.setValue(s20);
//				textField_department.setValue(s21);
				label_region.setValue("<font color='#2932F1'><b>" + s22 + "</b></font>");
//				textArea_remark.setValue(s23);
//				comboBox_branch.setData(s20);
				comboBox_branch.setValue(s20);
				comboBox_department.setValue(s21);
				textField_allwnce_1.setValue(s23);
				textField_allwnce_2.setValue(s24);
				dateField_startdte.setValue(rs.getDate("effective_date"));
				combobox_emptype.setValue(s30);
				textField_designtion.setValue(s29);
				conditions.setValue(s28);
				textField_montscunt.setValue(s25);

				label_Position_Confirmation.setValue("<font color='#2932F1'><b>" + s26 + "</b></font>");
//				label_Position_Confirmation.setValue(s26);
				otherBenefits.setValue(s27);

//				System.out.println(s20 + " " + s21);
			}

			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	void load_cv_number() {

		try {

			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			// comboBox_Cv_Nm.removeAllItems();
			
			
					
					ResultSet rs = st.executeQuery("SELECT ap.* FROM cv_create cr inner join cv_approval ap on cr.cv_no=ap.cv_no where cr.status='1'  order by cv_no asc LIMIT 1");
//			ResultSet rs = st.executeQuery("SELECT cv_no FROM cv_create where status='1'");

			while (rs.next()) {

				String rsv = rs.getString("cv_no");
				cv_no_temp = rsv;

				// button_accept.setEnabled(false);

			}

			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	void approval_save() {

		try {

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat db = new SimpleDateFormat("yyyy-MM-dd");
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			System.out.println("test insert 1");
			String query = "insert into cv_approval(cv_no, designation, barnch, department, offered_salary, user_name, emp_type, allownce_1, allownce_2, other_benifits, conditions, effective_date, months_count, position_confirmation, create_by, created_date) values('"
					+ viewstatus.getValue().toString() + "','" + textField_designtion.getValue().toUpperCase() + "','"
					+ comboBox_branch.getValue().toString() + "','" + comboBox_department.getValue().toString() + "','"
					+ textField_offed_salary.getValue() + "','" + username + "','"
					+ combobox_emptype.getValue().toString().toUpperCase() + "','" + textField_allwnce_1.getValue()
					+ "','" + textField_allwnce_2.getValue() + "','" + otherBenefits.getValue().trim().toUpperCase()
					+ "','" + conditions.getValue().trim().toUpperCase() + "','"
					+ db.format(dateField_startdte.getValue()) + "','" + textField_montscunt.getValue() + "','"
					+ label_Position_Confirmation.getValue() + "','" + username + "','" + df.format(new Date()) + "')";
			st.executeUpdate(query);
			System.out.println("test insert 1");
			cv_no_temp = viewstatus.getValue().toString();
			System.out.println("test insert 1");
			st.executeUpdate("update cv_create set status='1' where cv_no='" + viewstatus.getValue() + "'");
			System.out.println("test insert 1");
			// button_accept.setEnabled(false);
			Notification.show("Saved");
			load_cv_number();

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	void textFieldDisable() {
		try {
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			String sql = "SELECT user_leval FROM  interv_user where user_name='" + username + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Integer ulevl = rs.getInt("user_leval");
				if (ulevl >= 40) {
					System.out.println(ulevl);
					// Notification.show("right details here");
					textField_offed_salary.setEnabled(false);
					textField_allwnce_1.setEnabled(false);
					textField_allwnce_2.setEnabled(false);

					textField_designtion.setEnabled(false);
					combobox_emptype.setEnabled(false);
					dateField_startdte.setEnabled(false);
					textField_montscunt.setEnabled(false);
					button_date_cal.setEnabled(false);
					otherBenefits.setEnabled(false);
					conditions.setEnabled(false);
					comboBox_branch.setEnabled(false);
					comboBox_department.setEnabled(false);

				} else {
					textField_offed_salary.setEnabled(true);
				}
			}
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void getUserDetails(String user, String fullname, String call) {
		// TODO Auto-generated method stub
		username = user;
		full_name = fullname;
		call_Name = call;
		branch_load();
		department_load();
		// setdata();
		textFieldDisable();
		load_approval();

	}

	void load_approval() {

		try {
			Connection co = JDBC_MY.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery(
					"SELECT ap.* FROM cv_create cr inner join cv_approval ap on cr.cv_no=ap.cv_no where cr.status='1'  order by cv_no asc LIMIT 1");

//			Integer integer = Integer.parseInt(viewstatus.getValue());
//
//			ResultSet re = st.executeQuery("select * from cv_approval where cv_no='" + integer + "'");

			if (re.next()) {
				if (re.getString("hr_app") != null) {
					checkBox_hRM.setValue(false);
					checkBox_hRM.setIcon(fr);
					checkBox_hRM.setCaption(re.getString("hr_app"));
					checkBox_hRM.setEnabled(false);
				} else {
					checkBox_hRM.setValue(false);
					checkBox_hRM.setIcon(fr2);
					checkBox_hRM.setCaption("HR-MANAGER");
					checkBox_hRM.setEnabled(true);

				}
				if (re.getString("hod_app") != null) {
					checkBox_hOD.setValue(false);
					checkBox_hOD.setIcon(fr);
					checkBox_hOD.setCaption(re.getString("hod_app"));
					checkBox_hOD.setEnabled(false);
				} else {
					checkBox_hOD.setValue(false);
					checkBox_hOD.setIcon(fr2);
					checkBox_hOD.setCaption("Head Of Department");
					checkBox_hOD.setEnabled(true);
				}
				if (re.getString("director_app") != null) {
					checkBox_director.setValue(false);
					checkBox_director.setIcon(fr);
					checkBox_director.setCaption(re.getString("director_app"));
					checkBox_director.setEnabled(false);
				} else {
					checkBox_director.setValue(false);
					checkBox_director.setIcon(fr2);
					checkBox_director.setCaption("Director");
					checkBox_director.setEnabled(true);
				}
				if (re.getString("gm_app") != null) {
					checkBox_gM.setValue(false);
					checkBox_gM.setIcon(fr);
					checkBox_gM.setCaption(re.getString("gm_app"));
					checkBox_gM.setEnabled(false);
				} else {
					checkBox_gM.setValue(false);
					checkBox_gM.setIcon(fr2);
					checkBox_gM.setCaption("GM - Sales and Marketing");
					checkBox_gM.setEnabled(true);
				}
				if (re.getString("gm_tech_app") != null) {
					checkBox_gmtechnical.setValue(false);
					checkBox_gmtechnical.setIcon(fr);
					checkBox_gmtechnical.setCaption(re.getString("gm_tech_app"));
					checkBox_gmtechnical.setEnabled(false);
				} else {
					checkBox_gmtechnical.setValue(false);
					checkBox_gmtechnical.setIcon(fr2);
					checkBox_gmtechnical.setCaption("GM / AGM - Technical");
					checkBox_gmtechnical.setEnabled(true);
				}
				if (re.getString("ceo_app") != null) {
					checkBox_cEO.setValue(false);
					checkBox_cEO.setIcon(fr);
					checkBox_cEO.setCaption(re.getString("ceo_app"));
					checkBox_cEO.setEnabled(false);
				} else {
					checkBox_cEO.setValue(false);
					checkBox_cEO.setIcon(fr2);
					checkBox_cEO.setCaption("CEO");
					checkBox_cEO.setEnabled(true);
				}
				if (re.getString("md_app") != null) {
					checkBox_mD.setValue(false);
					checkBox_mD.setIcon(fr);
					checkBox_mD.setCaption(re.getString("md_app"));
					checkBox_mD.setEnabled(false);
				} else {
					checkBox_mD.setValue(false);
					checkBox_mD.setIcon(fr2);
					checkBox_mD.setCaption("Managing Director");
					checkBox_mD.setEnabled(true);
				}
				if (re.getString("chairman_app") != null) {
					checkBox_chairman.setValue(false);
					checkBox_chairman.setIcon(fr);
					checkBox_chairman.setCaption(re.getString("chairman_app"));
					checkBox_chairman.setEnabled(false);
				} else {
					checkBox_chairman.setValue(false);
					checkBox_chairman.setIcon(fr2);
					checkBox_chairman.setCaption("Chairman");
					checkBox_chairman.setEnabled(true);
				}
			} else {

				checkBox_hRM.setIcon(fr2);

				checkBox_chairman.setIcon(fr2);

				checkBox_mD.setIcon(fr2);

				checkBox_cEO.setIcon(fr2);

				checkBox_gmtechnical.setIcon(fr2);

				checkBox_gM.setIcon(fr2);

				checkBox_director.setIcon(fr2);

				checkBox_hOD.setIcon(fr2);
			}
		} catch (Exception e) {
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
				label_email.setValue(re.getString("e_mail"));
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
				viewstatus.setValue(re.getString("cv_no"));
				comboBox_department.setValue(re.getString("department"));
				combobox_emptype.setValue(re.getString("emp_mode"));

				textField_allwnce_1.setValue(re.getString("allownce_1"));
				textField_allwnce_2.setValue(re.getString("allownce_2"));
				textField_designtion.setValue(re.getString("designation"));
				textField_montscunt.setValue(re.getString("months_count"));
				textField_offed_salary.setValue(re.getString("offered_salary"));

				otherBenefits.setValue(re.getString("other_benifits"));
				conditions.setValue(re.getString("conditions"));

			}

			re.close();
			st.close();
			co.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	void approval_done() {
		try {
			int app = 0;
			Connection co = JDBC_MY.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery("select * from cv_approval where cv_no='" + cv_no_temp + "'");
		String	cnoo=	"";
		String	brac="";
		String	desi=	"";
		String	dpt="";
			if (re.next()) {
				if (re.getString("hr_app") != null) {
					app++;
				}
				if (re.getString("hod_app") != null) {
					app++;
				}
				if (re.getString("director_app") != null) {
					app++;
				}
				if (re.getString("gm_app") != null) {
					app++;
				}
				if (re.getString("ceo_app") != null) {
					app++;
				}
				if (re.getString("md_app") != null) {
					app++;
				}
				if (re.getString("chairman_app") != null) {
					app++;
				}
				if (re.getString("gm_tech_app") != null) {
					app++;
				}
			
			
			cnoo=	re.getString("cv_no");
			brac=	re.getString("barnch");
			desi=	re.getString("designation");
			dpt=	re.getString("department");
			
			}

//			String fPath=FileUtils.readFileToString(new File("\\\\\\172.20.10.16\\hr\\pathinter.txt"));
//			int appcount=Integer.parseInt(fPath);

			ResultSet re1 = st.executeQuery("SELECT board_member_count FROM board_member_count");
			if (re1.next()) {
				if (app == re1.getInt("board_member_count")) {
					Statement st1 = co.createStatement();
					st1.executeUpdate("update cv_create set status='2' where cv_no='" + cv_no_temp + "'");
					/// clear eka damma methanata
					//Notification.show("Successfully Approved", Type.ERROR_MESSAGE.WARNING_MESSAGE);
					
					
					
					Notification.show(("Successfully Approved" +""+ "\nEPN: "+ cnoo+"\nBranch: " + brac+ "\nDesignation: " + desi + "\nDepartment: "+ dpt).replaceAll("null", ""),	
					Notification.TYPE_TRAY_NOTIFICATION);
					
					
					cv_no_temp = "0";
					st1.close();
				}
			}
		
			
		//	re1.close();
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

		viewstatus.setValue("");
		label_civil_state.setValue("");
		label_company.setValue("");
		label_nameWithInitials.setValue("");
		label_telephoneNo.setValue("");
		label_address.setValue("");
		label_email.setValue("");
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
		textField_allwnce_1.setValue("");
		textField_allwnce_2.setValue("");
//		comboBox_designation.clear();
		textField_montscunt.clear();
		label_Position_Confirmation.setValue("");
		textField_designtion.setValue("");
		otherBenefits.setValue("");
		conditions.setValue("");
		comboBox_department.clear();
		combobox_emptype.clear();
		dateField_startdte.clear();
		comboBox_branch.setValue("");
		load_cv_number();
//		placeholder();
	}

	private void clear_All_Fields() {

		viewstatus.setValue("");
		label_civil_state.setValue("");
		label_company.setValue("");
		label_nameWithInitials.setValue("");
		label_telephoneNo.setValue("");
		label_address.setValue("");
		label_email.setValue("");
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
		textField_allwnce_1.setValue("");
		textField_allwnce_2.setValue("");
//		comboBox_designation.clear();
		textField_montscunt.clear();
		label_Position_Confirmation.setValue("");
		textField_designtion.setValue("");
		otherBenefits.setValue("");
		conditions.setValue("");
		comboBox_department.clear();
		combobox_emptype.clear();
		dateField_startdte.clear();
		comboBox_branch.setValue("");

		checkBox_hRM.setEnabled(true);
		checkBox_hRM.setValue(false);
		checkBox_mD.setEnabled(true);
		checkBox_mD.setValue(false);
		checkBox_cEO.setEnabled(true);
		checkBox_cEO.setValue(false);
		checkBox_chairman.setEnabled(true);
		checkBox_chairman.setValue(false);
		checkBox_director.setEnabled(true);
		checkBox_director.setValue(false);
		checkBox_gM.setEnabled(true);
		checkBox_gM.setValue(false);
		checkBox_gmtechnical.setEnabled(true);
		checkBox_gmtechnical.setValue(false);
		checkBox_hOD.setEnabled(true);
		checkBox_hOD.setValue(false);
		
		checkBox_hRM.setCaption("HR-MANAGER");
		checkBox_hOD.setCaption("Head Of Department");
		checkBox_director.setCaption("Director");
		checkBox_gM.setCaption("GM - Sales and Marketing");
		checkBox_chairman.setCaption("Chairman");
		checkBox_mD.setCaption("Managing Director");
		checkBox_cEO.setCaption("CEO");
		checkBox_gmtechnical.setCaption("GM / AGM - Technical");

//		load_cv_number();
//		placeholder();
	}

}
