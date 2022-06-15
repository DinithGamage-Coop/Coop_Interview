package lk.ci.int_cn_system.UI.Auth;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.digester.RegexMatcher;
import org.apache.commons.io.FileUtils;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Button.ClickEvent;

import lk.ci.int_cn_system.Model.Auth.UserResponseModel;
import lk.ci.int_cn_system.UI.DashBoardSub;
import lk.ci.int_cn_system.Utils.EmailValidator;
import lk.ci.int_cn_system.Utils.JDBC_COOP;
import lk.ci.int_cn_system.Utils.JDBC_MY;
import net.coobird.thumbnailator.Thumbnails;
import net.sf.jasperreports.engine.export.ResetableExporterFilter;

public class UserRegistrationSub extends UserRegistration {

	DashBoardSub dashBsub;
	Connection con;
	Statement st;
	String fPath;
	Upload upload;
	public Window uploader;
	private String CV_ID;

	private String call_name = null;

	private String username;

	private String full_name;

	public UserRegistrationSub() {

		button_update.setEnabled(false);
		LoadTable();
		placeholder();
		load_cv_number();

		textField_landline.setMaxLength(10);
		tp.setMaxLength(10);
		nic.setMaxLength(12);
		salary.setMaxLength(9);

		table1.addContainerProperty("CV NO", String.class, null);
		table1.addContainerProperty("Name With Initials", String.class, null);
		table1.addContainerProperty("NIC", String.class, null);
		table1.addContainerProperty("Gender", String.class, null);
		table1.addContainerProperty("Telephone No", String.class, null);
		table1.addContainerProperty("Address", String.class, null);
		table1.addContainerProperty("Requested Salary", String.class, null);
		table1.addContainerProperty("Action", HorizontalLayout.class, null);

		button_serch.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (textField_cv_no.getValue().equals("")) {
					Notification.show("Please Enter Correct CV Number");
				} else {
					button_update.setEnabled(true);
					button_save.setEnabled(false);
					setdata();
					LoadTable();
				}

			}
		});

		button_update.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if (comboBox_company.getValue() == null) {
					Notification.show("Please type the correct CV number ",
							Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
				} else {
					// Notification.show("Please type the land line Correct
					// format",Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					update();
					button_save.setEnabled(true);
					button_update.setEnabled(false);
					LoadTable();
				}

			}
		});

		button_clear.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (call_name == null) {
					new Notification("Invalid User", "<br/>please login correct user<br/>",
							Notification.TYPE_WARNING_MESSAGE.ERROR_MESSAGE, true).show(Page.getCurrent());

				}

				else {
					label_cVno.setValue("");
					clearFields();
					cv_no();
					button_save.setEnabled(true);
					button_update.setEnabled(false);
					LoadTable();
					Notification.show("successfully cleared", Notification.TYPE_HUMANIZED_MESSAGE);
				}

			}
		});

		comboBox_highestqualification.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});

		fullnm.addBlurListener(new FieldEvents.BlurListener() {

			@Override
			public void blur(BlurEvent event) {
				// TODO Auto-generated method stub

				String fname = fullnm.getValue();
				if (!fname.isEmpty()) {

					String[] nms = fname.split(" ");

					String name_int = "";

					int last = nms.length;
					int chk = 0;
					for (String n : nms) {
						chk = chk + 1;
						if (n.equals("DE")) {
							name_int = name_int + " " + n;
						} else {
							if (chk == last) {
								name_int = name_int + " " + n;
							} else {
								name_int = name_int + " " + n.substring(0, 1);
							}

						}

					}

					namewinti.setValue(name_int.substring(1));
//			           jTextField_FirstName.setText(name_int.substring(1));

				}
			}
		});
		
		
		button_ATT.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				setProfilePIC("change");
				
			}
		});

		button_save.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

				button_update.setEnabled(false);

				try {
					String mt1 = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";
					String NIC = nic.getValue().trim();
					EmailValidator emailValidator = new EmailValidator();
					if (call_name == null) {
						new Notification("Invalid User", "<br/>please login correct user<br/>",
								Notification.TYPE_WARNING_MESSAGE.ERROR_MESSAGE, true).show(Page.getCurrent());
					} else if (comboBox_company.getValue() == null) {
						Notification.show("Please select Company", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if (comboBox_empCategory.getValue() == null) {
						Notification.show("Please select Employee Category | Sales Or Non Sales",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if (comboBox_titel.getValue() == null) {
						Notification.show("Please select Title", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((fullnm.getValue().equals(""))) {
						Notification.show("Please Fill The Name", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((gender.getValue() == null)) {
						Notification.show("Please select Gender", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((address.getValue().equals(""))) {
						Notification.show("Please Fill the Address", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((tp.getValue().equals("")) || (tp.getValue().length() < 10)) {
						Notification.show("Telephone Number Empty Or Invalid",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((dateField_dob.getValue() == null)) {
						Notification.show("Please Fill the Date of Birth",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((nic.getValue().equals(""))) {
						Notification.show("Please Fill the Nic Number",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if (!NIC.matches(mt1)) {
						Notification.show("NIC is Wrong", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((comboBox_civilstate.getValue() == null)) {
						Notification.show("Please select the Civil State",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((comboBox_empmode.getValue() == null)) {
						Notification.show("Please select the Employee Mode",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((salary.getValue().equals(""))) {
						Notification.show("Please Fill the Expected Salary",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((comboBox_religion.getValue() == null)) {
						Notification.show("Please select the Religion",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((comboBox_highestqualification.getValue() == null)) {
						Notification.show("Please select the Highest Qualification",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					} else if ((requestingPossition.getValue().equals(""))) {
						Notification.show("Please Fill the Requesting Position",
								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
					}
//					else if (!emailValidator.validate(email.getValue().toString().trim())) {
//						Notification.show("Type email correct format", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
//					} 
//					else if ((textField_landline.getValue().equals(""))
//							|| (textField_landline.getValue().length() < 10)) {
//						Notification.show("Land Number Empty Or Invalid",
//								Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
//					} 

					else {
						cv_no();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						SimpleDateFormat db = new SimpleDateFormat("yyyy-MM-dd");
						Connection con = JDBC_MY.con();
						Statement st = con.createStatement();

						String cv = label_cVno.getValue().trim();
						String empcat = comboBox_empCategory.getValue().toString();
						String intdby = textField_introduceBy.getValue().trim().toUpperCase();
						String titel = comboBox_titel.getValue().toString();
						String dob = db.format(dateField_dob.getValue()).toUpperCase();
						String landl = textField_landline.getValue().toString().trim().toUpperCase();
						String civst = comboBox_civilstate.getValue().toString();
						String empmd = comboBox_empmode.getValue().toString();
//						String brnch = textField_branch.getValue().trim();
//						String dept = textField_department.getValue().trim();
						String regn = comboBox_religion.getValue().toString();
						String remk = textArea_remark.getValue().trim().toUpperCase();
						String fname = fullnm.getValue().trim().toUpperCase();

						String namwint = namewinti.getValue().trim().toUpperCase();
						String gendr = gender.getValue().toString();

						String telep = tp.getValue().trim().toString().trim();
						String EMAIL = email.getValue().trim().toUpperCase();
						String addres = address.getValue().trim().toUpperCase();
						String salry = salary.getValue().trim();
						String highqual = comboBox_highestqualification.getValue().toString();
						String requestPosit = requestingPossition.getValue().trim().toUpperCase();
						String workcmpny = textField_company.getValue().trim().toUpperCase();
						String workdesg = textField_designation.getValue().trim().toUpperCase();
						String worknoofyrs = textField_no_of_years.getValue().trim();

						String query = "insert into cv_create(cv_no,full_name,name_with_instials,gender,nic,tp_no,e_mail,address,salary,highest_qualification,requ_possition,create_by"
								+ ",created_date,company,current_holder,emp_category,introduce_by,titel,dob,land_line,civil_state,emp_mode,region,remark,work_company,work_designation,work_no_of_hours,upload_status) values"
								+ "('" + cv + "','" + fname + "','" + namwint + "','" + gendr + "'" + ",'" + NIC + "'"
								+ ",'" + telep + "','" + EMAIL + "'" + ",'" + addres + "','" + salry + "','" + highqual
								+ "'" + ",'" + requestPosit + "','" + username + "','" + df.format(new Date()) + "'"
								+ ",'" + comboBox_company.getValue().toString() + "','" + call_name + "','" + empcat
								+ "'" + ",'" + intdby + "','" + titel + "','" + dob + "','" + landl + "'," + "'" + civst
								+ "','" + empmd + "','" + regn + "','" + remk + "','" + workcmpny + "','" + workdesg
								+ "','" + worknoofyrs + "','"+0+"')";
						// ResultSet re = st.executeQuery("select cv_no from cv_create where cv_no='" +
						// label_cVno.getValue() + "' ");

						st.executeUpdate(query);

						cv_no();
						clearFields();
						LoadTable();
						load_cv_number();
						Notification.show("Successfully Saved ", Notification.TYPE_WARNING_MESSAGE);
//						new Notification("Invalid User","<br/>please login correct user<br/>",
//							    Notification.TYPE_WARNING_MESSAGE.WARNING_MESSAGE, true)
//							    .show(Page.getCurrent());
					}
					st.close();
					con.close();
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
					e.printStackTrace();
				}

			}

		});

	}

	public void search_validate() {

		try {

			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			String sql = "SELECT cv_no FROM cv_create";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void update() {

		try {
			String mt1 = "^([0-9]{9}[x|X|v|V]|[0-9]{12})$";
			String NIC = nic.getValue().trim().toUpperCase();
			EmailValidator emailValidator = new EmailValidator();
			if (call_name == null) {
				new Notification("Invalid User", "<br/>please login correct user<br/>",
						Notification.TYPE_WARNING_MESSAGE.ERROR_MESSAGE, true).show(Page.getCurrent());
			} else if (textField_cv_no.getValue() == null) {
				Notification.show("Please Search the Correct CV Number",
						Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if (comboBox_company.getValue() == null) {
				Notification.show("Please select Company", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if (comboBox_empCategory.getValue() == null) {
				Notification.show("Please select Employee Category | Sales Or Non Sales",
						Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if (comboBox_titel.getValue() == null) {
				Notification.show("Please select Title", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((fullnm.getValue().equals(""))) {
				Notification.show("Please Fill The Name", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((gender.getValue() == null)) {
				Notification.show("Please select Gender", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((address.getValue().equals(""))) {
				Notification.show("Please Fill the Address", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((tp.getValue().equals("")) || (tp.getValue().length() < 10)) {
				Notification.show("Telephone Number Empty Or Invalid", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((dateField_dob.getValue() == null)) {
				Notification.show("Please Fill the Date of Birth", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((nic.getValue().equals(""))) {
				Notification.show("Please Fill the Nic Number", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if (!NIC.matches(mt1)) {
				Notification.show("NIC is Wrong", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((comboBox_civilstate.getValue() == null)) {
				Notification.show("Please select the Civil State", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((comboBox_empmode.getValue() == null)) {
				Notification.show("Please select the Employee Mode", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((salary.getValue().equals(""))) {
				Notification.show("Please Fill the Expected Salary", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((comboBox_religion.getValue() == null)) {
				Notification.show("Please select the Religion", Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((comboBox_highestqualification.getValue() == null)) {
				Notification.show("Please select the Highest Qualification",
						Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			} else if ((requestingPossition.getValue().equals(""))) {
				Notification.show("Please Fill the Requesting Position",
						Notification.TYPE_ERROR_MESSAGE.WARNING_MESSAGE);
			}

			else {
				cv_no();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat db = new SimpleDateFormat("yyyy-MM-dd");
				Connection con = JDBC_MY.con();
				Statement st = con.createStatement();

				String cv = label_cVno.getValue().trim();
				String empcat = comboBox_empCategory.getValue().toString();
				String intdby = textField_introduceBy.getValue().trim().toUpperCase();
				String titel = comboBox_titel.getValue().toString();
				String dob = db.format(dateField_dob.getValue()).toUpperCase();
				String landl = textField_landline.getValue().toString();
				String civst = comboBox_civilstate.getValue().toString();
				String empmd = comboBox_empmode.getValue().toString();
//				String brnch = textField_branch.getValue().trim();
//				String dept = textField_department.getValue().trim();
				String regn = comboBox_religion.getValue().toString();
				String remk = textArea_remark.getValue().trim().toUpperCase();
				String fname = fullnm.getValue().trim().toUpperCase();

				String namwint = namewinti.getValue().trim().toUpperCase();
				String gendr = gender.getValue().toString();

				String telep = tp.getValue().trim().toString().trim();
				String EMAIL = email.getValue().trim().toUpperCase();
				String addres = address.getValue().trim().toUpperCase();
				String salry = salary.getValue().trim();
				String highqual = comboBox_highestqualification.getValue().toString();
				String requestPosit = requestingPossition.getValue().trim().toUpperCase();

				String workcmpny = textField_company.getValue().trim().toUpperCase();
				String workdesg = textField_designation.getValue().trim().toUpperCase();
				String worknoofyrs = textField_no_of_years.getValue().toString().trim();

				{
//					st.executeUpdate("update cv_create set full_name='" + fname + "',name_with_instials='" + namwint
//							+ "'," + "gender='" + gendr + "',nic='" + NIC + "',tp_no='" + telep + "',e_mail='" + EMAIL
//							+ "',address='" + addres + "'," + "salary='" + salry + "',highest_qualification='"
//							+ highqual + "',requ_possition='" + requestPosit + "'," + "company='"
//							+ comboBox_company.getValue() + "',emp_category='" + empcat + "',introduce_by='" + intdby
//							+ "'," + "titel='" + titel + "',dob='" + dob + "',land_line='" + landl + "',civil_state='"
//							+ civst + "'," + "emp_mode='" + empmd + "',region='" + regn + "',remark='" + remk
//							+ "'where cv_no='" + textField_cv_no.getValue() + "'");
					st.executeUpdate("update cv_create set full_name='" + fname + "',name_with_instials='" + namwint
							+ "'," + "gender='" + gendr + "',nic='" + NIC + "',tp_no='" + telep + "',e_mail='" + EMAIL
							+ "',address='" + addres + "'," + "salary='" + salry + "',highest_qualification='"
							+ highqual + "',requ_possition='" + requestPosit + "'," + "company='"
							+ comboBox_company.getValue() + "',emp_category='" + empcat + "',introduce_by='" + intdby
							+ "'," + "titel='" + titel + "',dob='" + dob + "',land_line='" + landl + "',civil_state='"
							+ civst + "'," + "emp_mode='" + empmd + "',region='" + regn + "',remark='" + remk
							+ "',work_company='" + workcmpny + "',work_designation='" + workdesg
							+ "',work_no_of_hours='" + worknoofyrs + "' where cv_no='" + textField_cv_no.getValue()
							+ "'");

				}

				cv_no();
				clearFields();
				LoadTable();

//				Notification.show("Username Is Empty", Notification.TYPE_TRAY_NOTIFICATION);
				Notification.show("Successfully Updated ", Notification.TYPE_WARNING_MESSAGE);
//				new Notification("Invalid User","<br/>please login correct user<br/>",
//					    Notification.TYPE_WARNING_MESSAGE.WARNING_MESSAGE, true)
//					    .show(Page.getCurrent());
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			e.printStackTrace();
		}

	}

	void settable() {
		try {
			table1.removeAllItems();

			table1.addItem(new Object[] { fullnm.getValue(), namewinti.getValue(), gender.getValue(), nic.getValue(),
					tp.getValue(), address.getValue(), salary.getValue() }, table1.size());

			// ,email.getValue(),highestQualification.getValue(),requestingPossition.getValue()

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LoadTable();
		}

	}

	private void clearFields() {

		textField_cv_no.clear();
		fullnm.clear();
		namewinti.clear();
		nic.clear();
		gender.clear();
		tp.clear();
		email.clear();
		address.clear();
		salary.clear();
		comboBox_company.clear();
		comboBox_highestqualification.clear();
		requestingPossition.clear();
		comboBox_empCategory.clear();
		textField_introduceBy.clear();
		comboBox_titel.clear();
		dateField_dob.clear();
		textField_landline.clear();
		comboBox_civilstate.clear();
		comboBox_empmode.clear();
//		textField_branch.clear();
//		textField_department.clear();
		comboBox_religion.clear();
		textArea_remark.clear();
		textField_company.clear();
		textField_designation.clear();
		textField_no_of_years.clear();

	}

	void LoadTable() {
		table1.removeAllItems();
		try {
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			String sql = "SELECT * FROM cv_create";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				String s1 = rs.getString("cv_no");
				String s3 = rs.getString("name_with_instials");
				String s4 = rs.getString("nic");
				String s5 = rs.getString("gender");
				String s6 = rs.getString("tp_no");
				String s8 = rs.getString("address");
				String s9 = rs.getString("salary");
				rs.getString("name_with_instials");
				rs.getString("nic");
				rs.getString("gender");
				rs.getString("tp_no");
				rs.getString("address");
				rs.getString("salary");
				HorizontalLayout layout = new HorizontalLayout();
				Button cnview_btn = new Button();
				cnview_btn.setStyleName("primary");
				cnview_btn.setCaption("View");

				cnview_btn.addClickListener(new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						// TODO Auto-generated method stub
						cnview_btn.setEnabled(true);
					}
				});
				layout.addComponent(cnview_btn, 0);
				table1.addItem(new Object[] { s1, s3, s4, s5, s6, s8, s9, layout }, table1.size());
			}

			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static Notification mesageHr() throws Exception {
		Notification n = new Notification("HR Manager Submited Details");
		return n;
	}

	void fileUpload() {

		class FileUploader implements Receiver, SucceededListener {

			private static final long serialVersionUID = 1L;
			public File folder;
			public File file;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {

				FileOutputStream fos = null;

				try {

					folder = new File("C:\\");

					boolean bool = folder.mkdirs();

					file = new File(folder, filename);

					fos = new FileOutputStream(file);

				} catch (final java.io.FileNotFoundException e) {

					new Notification("Could not open file", e.getMessage(), Notification.Type.ERROR_MESSAGE)
							.show(Page.getCurrent());

					return null;

				}

				return fos;

			}

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				// Show the uploaded file in the image viewer
//				readExcel(file);

				try {
					// FileOutputStream out = new
					// FileOutputStream("E:\\EXCEL\\"+toyear+"-"+tomonth+"-"+branch+".xls");
//					fPath = FileUtils.readFileToString(new File("\\\\\\172.20.10.14\\general\\HR\\doc4to_path.txt"));
					fPath = FileUtils.readFileToString(new File("\\172.20.10.14\\HR_File\\coop_interview_cv"));

					/// fPath=FileUtils.readFileToString(new
					/// File("\\\\172.20.10.20\\general\\dist\\doc4to_path.txt"));
				} catch (IOException ex) {
					ex.printStackTrace();
				}

			}
		}
		;

		FileUploader receiver = new FileUploader();

		file_upload.setReceiver(receiver);
		file_upload.addSucceededListener(receiver);

	}

	public

//	try {
//
//        
//      //  FileDialog fd = new FileDialog(this, "Select scanned images", FileDialog.LOAD);
//           
//          
//          // jFileChooser1_IMG.setDialogTitle("Select scanned images");
//           FileNameExtensionFilter ff=new FileNameExtensionFilter("Image Files", "bmp","jpg","jpeg","png");
//           
//           
//           
//           file_upload.setFileFilter(ff);
//           fd.setFile("*.jpg;*.jpeg;*.JPG;*.JPEG;*.BMP;*.bmp;*.png;*.PNG");
//           fd.setMultipleMode(true);
//           fd.setIconImage(this.getIconImage());
//          // jFileChooser1_IMG.setFileSelectionMode(JFileChooser.FILES_ONLY);
//         //  jFileChooser1_IMG.setMultiSelectionEnabled(true);
//         fd.setVisible(true);
//         
////           int resp=jFileChooser1_IMG.showOpenDialog(null);
////           
////           if(resp==JFileChooser.APPROVE_OPTION){
//
//               DefaultTableModel dim=(DefaultTableModel)jTable1_image_files.getModel();
//              // File[] sel_f=jFileChooser1_IMG.getSelectedFiles();
//              File[] sel_f=fd.getFiles();
//               for(File fn:sel_f){
//                   Vector vv=new Vector();
//                   vv.add(fn.getAbsolutePath()); vv.add(jComboBox1_DOC_TYPE.getSelectedItem().toString());
//                   dim.addRow(vv);
//               }
//
//        //   }
//
//       } catch (Exception e) {
//           e.printStackTrace();
//           JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
//       }
//	

	void setProfilePIC(String what) {

		try {
			if (what.equals("change")) {

				// upload.setButtonCaption("Uploading");
				// Implement both receiver that saves upload in a file and
				// listener for successful upload
				class ImageUploader implements Receiver, SucceededListener {
					/**
					 ** 
					 **/
					private static final long serialVersionUID = 1L;
					public File file;

					@Override
					public OutputStream receiveUpload(String filename, String mimeType) {
						// Create upload stream
						FileOutputStream fos = null; // Stream to write to
						try {
							Notification.show("uploading...");
							// Open the file for writing.
//						 
							
							file = new File("\\\\172.20.10.14\\HR_File\\coop_interview_cv\\"
									+comboBox_uploadcv.getValue().toString().trim() + ".pdf");
							
							fos = new FileOutputStream(file);
						} catch (final java.io.FileNotFoundException e) {
							new Notification("Could not open file ", e.getMessage(), Notification.Type.ERROR_MESSAGE)
									.show(Page.getCurrent()); 
							return null;
						}
						return fos; // Return the output stream to write to
					}

					@Override
					public void uploadSucceeded(SucceededEvent event) {
						// Show the uploaded file in the image viewer

						try {
							uploader.close();
							Notification.show("Uploaded !  ");
							

							// A
							DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

							try {
								Timestamp timestamp3 = new Timestamp(System.currentTimeMillis());

								Connection co = JDBC_MY.con();
								Statement st = co.createStatement(); 
								st.close();
								co.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
 
							java.io.File photo = new java.io.File("\\\\172.20.10.14\\HR_File\\coop_interview_cv\\"
									+comboBox_uploadcv.getValue().toString().trim() + ".pdf"); 
							if (photo.exists()) {
								 File ife=new
								 File("C:\\APPS\\tmp_hr_img\\"+System.currentTimeMillis()+".pdf"); 
								FileResource fr = new FileResource(photo);

								fr.setCacheTime(0); 
								clear_cv_number();
								comboBox_uploadcv.removeAllItems();
								load_cv_number();

							}

						} catch (Exception e) {
							// TODO Auto-generated catch block
							new Notification("Could not open file ", e.getMessage(), Notification.Type.ERROR_MESSAGE)
									.show(Page.getCurrent());
							// upload.setButtonCaption("Start Upload");
							e.printStackTrace();
						}

					}
				}
				;
				ImageUploader receiver = new ImageUploader();
				upload = new Upload("Upload Image Here", receiver);
				upload.setButtonCaption("Start Upload");
				upload.addSucceededListener(receiver);
				uploader = new Window("Uploader");
				VerticalLayout vt = new VerticalLayout();
				vt.addComponent(upload);
				uploader.setContent(vt);
				uploader.setWidth("400px");
				uploader.setHeight("150px");
				uploader.setModal(true);
				uploader.setResizable(false);
				UI.getCurrent().addWindow(uploader);
				

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Notification.show(e.toString(), Notification.Type.ERROR_MESSAGE);
		}

	}
	
	
	
	void placeholder() {
		textField_cv_no.setInputPrompt("CV00000");
	}

	void validation() {
		if (tp.getMaxLength() < 10) {
			Notification.show("Please Enter Correct Email Number", Type.ERROR_MESSAGE);
		}
	}

	void load_cv_number() {
		try {
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			comboBox_uploadcv.removeAllItems();
			ResultSet rs = st.executeQuery("SELECT cv_no FROM cv_create where upload_status='0'");			 
			while (rs.next()) {
				String rsv = rs.getString("cv_no");
				comboBox_uploadcv.addItem(rsv);
			}
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	void clear_cv_number() {
		try {
			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();		 
			st.executeUpdate("update cv_create set upload_status='1' where cv_no='" + comboBox_uploadcv.getValue() + "'"); 
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	void setdata() {
		table1.removeAllItems();
		LoadTable();

		try {

			Connection con = JDBC_MY.con();
			Statement st = con.createStatement();
			String sql = "SELECT * FROM cv_create where cv_no='" + textField_cv_no.getValue() + "' ";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {

				String s1 = rs.getString("cv_no");
				String s2 = rs.getString("full_name");
				String s3 = rs.getString("name_with_instials");
				String s4 = rs.getString("nic");
				String s5 = rs.getString("gender");
				String s6 = rs.getString("tp_no");
				String s7 = rs.getString("e_mail");
				String s8 = rs.getString("address");
				String s9 = rs.getString("salary");
				String s10 = rs.getString("company");
				String s11 = rs.getString("highest_qualification");
				String s12 = rs.getString("requ_possition");
				String s13 = rs.getString("emp_category");
				String s14 = rs.getString("introduce_by");
				String s15 = rs.getString("titel");
				String s17 = rs.getString("civil_state");
				String s18 = rs.getString("land_line");
				String s19 = rs.getString("emp_mode");
//				String s20 = rs.getString("branch");
//				String s21 = rs.getString("department");
				String s22 = rs.getString("region");
				String s23 = rs.getString("remark");
				String s24 = rs.getString("work_company");
				String s25 = rs.getString("work_designation");
				String s26 = rs.getString("work_no_of_hours");

				label_cVno.setValue(s1);
				fullnm.setValue(s2);
				namewinti.setValue(s3);
				nic.setValue(s4);
				gender.setValue(s5);
				tp.setValue(s6);
				email.setValue(s7);
				address.setValue(s8);
				salary.setValue(s9);
				comboBox_company.setValue(s10);
				comboBox_highestqualification.setValue(s11);
				requestingPossition.setValue(s12);
				comboBox_empCategory.setValue(s13);
				textField_introduceBy.setValue(s14);
				comboBox_titel.setValue(s15);
				dateField_dob.setValue(rs.getDate("dob"));
				comboBox_civilstate.setValue(s17);
				textField_landline.setValue(s18);
				comboBox_empmode.setValue(s19);
//				textField_branch.setValue(s20);
//				textField_department.setValue(s21);
				comboBox_religion.setValue(s22);
				textArea_remark.setValue(s23);
				textField_company.setValue(s24);
				textField_designation.setValue(s25);
				textField_no_of_years.setValue(s26);

			}

			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void cv_no() {
		LoadTable();
		Connection co = null;
		Statement st = null;
		ResultSet re = null;
		try {
			co = JDBC_MY.con();
			st = co.createStatement();
			re = st.executeQuery("SELECT max(cv_no) FROM cv_create");
			String inv_final;
			while (re.next()) {

				String inv = re.getString("max(cv_no)");
				System.out.println(inv);
				if (inv == null) {
					inv = "000000";

					int inv_value1 = Integer.parseInt(inv);
					int inv_value2 = inv_value1 + 1;

					inv_final = String.valueOf(inv_value2);
					DecimalFormat dcf = new DecimalFormat("000000");

					CV_ID = ("CV" + dcf.format(inv_value2));
				} else {

					int inv_value1 = Integer.parseInt(inv.substring(2));
					int inv_value2 = inv_value1 + 1;

					inv_final = String.valueOf(inv_value2);
					DecimalFormat dcf = new DecimalFormat("000000");

					CV_ID = ("CV" + dcf.format(inv_value2));

				}
				label_cVno.setValue(CV_ID);
			}

			re.close();
			st.close();
			co.close();

		} catch (Exception e) {
			// logger.error("ERROR_ID", ex);
			CV_ID = ("CV" + 000001);
			label_cVno.setValue(CV_ID);
			System.out.println(e);
			e.printStackTrace();
			Notification.show("SAVE ERROR---!--CALL CICLIT", Notification.TYPE_ERROR_MESSAGE);
			// TODO: handle exception
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
			}

			else {
				Notification.show("User not Valid Access Denied", "Add user Name to Application",
						Notification.TYPE_ERROR_MESSAGE);
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

	public void getUserDetails(String user, String fullname) {
		// TODO Auto-generated method stub
		username = user;
		full_name = fullname;
		cv_no();
		LoadTable();
		call_name(username);
//		highest_designation_load();

	}

}
