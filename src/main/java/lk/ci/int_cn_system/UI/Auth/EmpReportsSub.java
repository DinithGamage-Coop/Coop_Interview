package lk.ci.int_cn_system.UI.Auth;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ResultSetDynaClass;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;

import lk.ci.int_cn_system.UI.SimQuotation.SysPath;
import lk.ci.int_cn_system.Utils.JDBC_MY;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

public class EmpReportsSub extends EmpReports {

	public EmpReportsSub() {

		button_single_print.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("#################################################");
				if (comboBox_cv_no.getValue() == null) {
					System.out.println("#################################################");
					Notification.show("Select the CV no");

				}

				else {
					System.out.println("aaaaaaaaaaa1111");
					try {

						String defult = "Untitled";
						Connection co = JDBC_MY.con();
						Statement st = co.createStatement();
						ResultSet re = st.executeQuery(
								"SELECT * FROM cv_create cr inner join cv_approval ap on cr.cv_no=ap.cv_no where cr.cv_no='"
										+ comboBox_cv_no.getValue() + "' ");
						if (re.next()) {
							System.out.println("aaaaaaaaaaa1111");
							// JRTableModelDataSource datajr=new JRTableModelDataSource(jTable1.getModel());
							String rep = "C:/APPS/JASPER_REPORTS/hr/report/Coop_interviews.jrxml";
							Map<String, Object> para = new HashMap<String, Object>();
							SimpleDateFormat db = new SimpleDateFormat("yyyy-MM-dd");
							para.put("date", db.format(re.getDate("ap.created_date")));
							para.put("effctive_date", db.format(re.getDate("ap.effective_date")));
							para.put("name", re.getString("name_with_instials"));
							para.put("fullname", re.getString("cr.full_name"));
							para.put("address", re.getString("address"));
							para.put("age", re.getString("dob"));
							para.put("introduce", re.getString("introduce_by"));
							para.put("designation", re.getString("designation"));
							para.put("salary", re.getString("ap.offered_salary"));
							para.put("allownce_1", re.getString("allownce_1"));
							para.put("travaling", re.getString("allownce_2"));
							para.put("barnch", re.getString("barnch"));
							// System.out.println("aaaaaaaaaaa"+re.getString("ap.monthly_target"));
							para.put("monthly_target", re.getString("ap.monthly_target"));
							para.put("period", re.getString("ap.period"));
							para.put("work_company", re.getString("work_company"));
							para.put("work_designation", re.getString("work_designation"));
							para.put("work_no_of_hours", re.getString("work_no_of_hours"));
							para.put("other_benifits", re.getString("other_benifits"));
							para.put("conditions", re.getString("conditions"));

							String dp = "";
							if (re.getString("ap.department").equals("SELECT")) {
								dp = "";
							} else {
								dp = re.getString("ap.department");
							}

							para.put("dpt", dp);
							para.put("nic", re.getString("nic"));
							para.put("remark", re.getString("remark"));
							para.put("chairman_app",
									re.getString("chairman_app") == null ? defult : re.getString("chairman_app"));
							para.put("md_app", re.getString("md_app") == null ? defult : re.getString("md_app"));
							para.put("ceo_app", re.getString("ceo_app") == null ? defult : re.getString("ceo_app"));
							para.put("gm_app", re.getString("gm_app") == null ? defult : re.getString("gm_app"));
							para.put("director_app",
									re.getString("director_app") == null ? defult : re.getString("director_app"));
							para.put("hod_app", re.getString("hod_app") == null ? defult : re.getString("hod_app"));
							para.put("hr_app", re.getString("hr_app") == null ? defult : re.getString("hr_app"));
							para.put("gm_tech_app",
									re.getString("gm_tech_app") == null ? defult : re.getString("gm_tech_app"));
							para.put("gender", re.getString("gender"));
							para.put("cv_no", comboBox_cv_no.getValue());
							/*
							 * para.put("user", re.getString("name_with_instials")); para.put("date",
							 * re.getString("name_with_instials")); para.put("amt_range",
							 * re.getString("name_with_instials")); para.put("si_no",
							 * re.getString("name_with_instials"));
							 */
							JasperReport jrr = JasperCompileManager.compileReport(rep);
							JasperPrint jpr = JasperFillManager.fillReport(jrr, para);

							URL addr = new URL("file:///C:/APPS/tmp_img/" + System.currentTimeMillis() + ".pdf");
							File pdf = new File(addr.toURI());

							JasperExportManager.exportReportToPdfStream(jpr, new FileOutputStream(pdf));
							// System.out.println("aaa"+re.getString("department"));
							FileResource fr = new FileResource(pdf);
							getUI().getPage().open(fr, "_blank", true);
						}
						re.close();
						st.close();
						co.close();
						clear();
						
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				}
			}

		});

		button_print.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				Connection con = null;
				Statement st = null;
				ResultSet rs = null;
				SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd ");

				try {

					//String filename = SysPath.GetSysPath() + "/EXCELL/" + "List of Interview"+ System.currentTimeMillis() + ".xlsx";
					String filename = SysPath.GetSysPath() + "/EXCELL/" + "List of Interview.xlsx";

					// String filename = System.getProperty("user.home") + "/Desktop/" +
					// "/FINALSAL/" +"ot"+System.currentTimeMillis()+".xlsx";
					
					XSSFWorkbook workbook = new XSSFWorkbook();
					XSSFSheet sheet = workbook.createSheet("Coop_Interview");

					XSSFRow rowhead = sheet.createRow(0);
					rowhead.createCell(0).setCellValue("CV_No");
					rowhead.createCell(1).setCellValue("Titel");
					rowhead.createCell(2).setCellValue("Full Name");
					rowhead.createCell(3).setCellValue("Name With Initials");
					rowhead.createCell(4).setCellValue("NIC");
					rowhead.createCell(5).setCellValue("Address");
					rowhead.createCell(6).setCellValue("Gender");
					rowhead.createCell(7).setCellValue("Mobile No");
					rowhead.createCell(8).setCellValue("Land No");
					rowhead.createCell(9).setCellValue("Date Of Birth");
					rowhead.createCell(10).setCellValue("Region");
					rowhead.createCell(11).setCellValue("Civil State");
					rowhead.createCell(12).setCellValue("E-Mail");
					rowhead.createCell(13).setCellValue("Company");
					rowhead.createCell(14).setCellValue("Emp Type");
					rowhead.createCell(15).setCellValue("Months Count");
					rowhead.createCell(16).setCellValue("Emp Category");
					rowhead.createCell(17).setCellValue("Salary");
					rowhead.createCell(18).setCellValue("Allowance 1");
					rowhead.createCell(19).setCellValue("Allowance 2");
					rowhead.createCell(20).setCellValue("Branch");
					rowhead.createCell(21).setCellValue("Department");
					rowhead.createCell(22).setCellValue("Designation");
					rowhead.createCell(23).setCellValue("Effectiv Date");
					rowhead.createCell(24).setCellValue("Highest Qualification");
					rowhead.createCell(25).setCellValue("Work Company");
					rowhead.createCell(26).setCellValue("Work Designation");
					rowhead.createCell(27).setCellValue("Work Years");
					rowhead.createCell(28).setCellValue("Conditions");
					rowhead.createCell(29).setCellValue("Other Benifits");
					rowhead.createCell(30).setCellValue("Position Confirmation");
					rowhead.createCell(31).setCellValue("Monthly Traget");
					rowhead.createCell(32).setCellValue("Period");
					
					


					sheet.setColumnWidth(0, 3000);
					sheet.setColumnWidth(1, 1500);
					sheet.setColumnWidth(2, 18000);
					sheet.setColumnWidth(3, 7000);
					sheet.setColumnWidth(4, 4000);
					sheet.setColumnWidth(5, 20000);
					sheet.setColumnWidth(6, 3500);
					sheet.setColumnWidth(7, 3500);
					sheet.setColumnWidth(8, 3500);
					sheet.setColumnWidth(9, 3500);
					sheet.setColumnWidth(10, 5000);
					sheet.setColumnWidth(11, 4000);
					sheet.setColumnWidth(12, 6000);
					sheet.setColumnWidth(13, 3000);
					sheet.setColumnWidth(14, 4000);
					sheet.setColumnWidth(15, 4000);
					sheet.setColumnWidth(16, 4000);
					sheet.setColumnWidth(17, 4000);
					sheet.setColumnWidth(18, 6000);
					sheet.setColumnWidth(19, 6000);
					sheet.setColumnWidth(20, 6000);
					sheet.setColumnWidth(21, 7000);
					sheet.setColumnWidth(22, 10000);
					sheet.setColumnWidth(23, 6000);
					sheet.setColumnWidth(24, 7000);
					sheet.setColumnWidth(25, 8000);
					sheet.setColumnWidth(26, 8000);
					sheet.setColumnWidth(27, 6000);
					sheet.setColumnWidth(28, 20000);
					sheet.setColumnWidth(29, 20000);
					sheet.setColumnWidth(30, 6000);
					sheet.setColumnWidth(31, 4000);
					sheet.setColumnWidth(32, 3000);



					Connection co = JDBC_MY.con();
					st = co.createStatement();

					rs = st.executeQuery(
							"SELECT * FROM cv_create cr inner join cv_approval ap on cr.cv_no=ap.cv_no where  ap.created_date between '"
									+ dat.format(dateField_from.getValue()) + "' and '"
									+ dat.format(dateField_to.getValue()) + "' ");

					int i = 0;
					while (rs.next()) {
						i = i + 1;
						String cvno = rs.getString("cr.cv_no");
						String titel = rs.getString("cr.titel");
						String fname = rs.getString("cr.full_name");
						String namewthni = rs.getString("cr.name_with_instials");
						String niC = rs.getString("cr.nic");
						String addrs = rs.getString("cr.address");
						String gndr = rs.getString("cr.gender");
						String tpnm = rs.getString("cr.tp_no");
						String lndnm = rs.getString("cr.land_line");
						String dob = rs.getString("cr.dob");
						String regin = rs.getString("cr.region");
						String civilst = rs.getString("cr.civil_state");
						String email = rs.getString("cr.e_mail");
						String compny = rs.getString("cr.company");
						String emptyp = rs.getString("ap.emp_type");
						String empcatg = rs.getString("cr.emp_category");
						String salry = rs.getString("ap.offered_salary");
						String allw1 = rs.getString("ap.allownce_1");
						String allw2 = rs.getString("ap.allownce_2");
						String brnch = rs.getString("ap.barnch");
						String dptmnt = rs.getString("ap.department");
						String design = rs.getString("ap.designation");
						String effdate = rs.getString("ap.effective_date");
						String moncunt = rs.getString("ap.months_count");
						String higstq = rs.getString("cr.highest_qualification");
						String workcomp = rs.getString("cr.work_company");
						String workdesig = rs.getString("cr.work_designation");
						String workyers = rs.getString("cr.work_no_of_hours");
						String othrbnft = rs.getString("ap.other_benifits");
						String cndtion = rs.getString("ap.conditions");
						String possconf = rs.getString("ap.position_confirmation");
						String montrgt = rs.getString("ap.monthly_target");
						String priod = rs.getString("ap.period");

						//System.out.println("ffffff" + cvno);

						// System.out.println("data exist..............");
						XSSFRow row = sheet.createRow(i);

						row.createCell(0).setCellValue(cvno);
						row.createCell(1).setCellValue(titel);
						row.createCell(2).setCellValue(fname);
						row.createCell(3).setCellValue(namewthni);
						row.createCell(4).setCellValue(niC);
						row.createCell(5).setCellValue(addrs);
						row.createCell(6).setCellValue(gndr);
						row.createCell(7).setCellValue(tpnm);
						row.createCell(8).setCellValue(lndnm);
						row.createCell(9).setCellValue(dob);
						row.createCell(10).setCellValue(regin);
						row.createCell(11).setCellValue(civilst);
						row.createCell(12).setCellValue(email);
						row.createCell(13).setCellValue(compny);
						row.createCell(14).setCellValue(emptyp);
						row.createCell(15).setCellValue(moncunt);
						row.createCell(16).setCellValue(empcatg);
						row.createCell(17).setCellValue(salry);
						row.createCell(18).setCellValue(allw1);
						row.createCell(19).setCellValue(allw2);						
						row.createCell(20).setCellValue(brnch);
						row.createCell(21).setCellValue(dptmnt);
						row.createCell(22).setCellValue(design);
						row.createCell(23).setCellValue(effdate);
						row.createCell(24).setCellValue(higstq);						
						row.createCell(25).setCellValue(workcomp);
						row.createCell(26).setCellValue(workdesig);
						row.createCell(27).setCellValue(workyers);
						row.createCell(28).setCellValue(cndtion);
						row.createCell(29).setCellValue(othrbnft);
						row.createCell(30).setCellValue(possconf);
						row.createCell(31).setCellValue(montrgt);
						row.createCell(32).setCellValue(priod);
					

					}

					FileOutputStream fileOut = new FileOutputStream(filename);
					workbook.write(fileOut);
					fileOut.close();

					FileResource frr = new FileResource(new File(filename));
					getUI().getPage().open(frr, "_blank", true);
					clear();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					
				}

			}
		});

	}


	public void getUserDetails(String username, String full_name, String call_name) {
		cv_load();
		// TODO Auto-generated method stub

	}

	public void cv_load() {
		try {
			comboBox_cv_no.removeAllItems();
			Connection co = JDBC_MY.con();
			Statement st = co.createStatement();
			ResultSet re = st.executeQuery("SELECT * FROM e_details.cv_create where status='2' ");
			while (re.next()) {
				comboBox_cv_no.addItem(re.getString("cv_no"));

			}
			re.close();
			st.close();
			co.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}
	
	
	public void clear() {
		
		comboBox_cv_no.clear();
		dateField_from.clear();
		dateField_to.clear();
			
		
	}

}

