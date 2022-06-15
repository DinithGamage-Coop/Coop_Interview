package lk.ci.int_cn_system.UI.Auth;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.sass.internal.tree.controldirective.IfElseDefNode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import lk.ci.int_cn_system.Utils.JDBC_MY;

public class MySub extends Window {

	public MySub() {

		super("Add New Designation ");
		center();

		VerticalLayout content = new VerticalLayout();
		content.setEnabled(true);
		content.addComponent(new Label("Type New Designation Here"));
		content.setMargin(true);
		content.setHeight("200px");
		content.setWidth("400px");
		setContent(content);

		setClosable(true);

		TextField tf = new TextField();
		tf.setWidth("370px");
		content.addComponent(tf);

		Button ok = new Button("Add");
		ok.setWidth("370px");
		ok.setStyleName("primary");
		ok.setResponsive(true);

		Button ok1 = new Button("close");
		ok1.setWidth("370px");
		ok1.setStyleName("danger");

		ok1.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				close(); // Close the sub-window
			}
		});

		content.addComponent(ok);
		content.addComponent(ok1);

		ok.addClickListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				
				
				if (tf.isEmpty()) {
					Notification.show("Fill the Data",Notification.TYPE_WARNING_MESSAGE);
					
				}
				
				else {
					try {
						Connection con = JDBC_MY.con();
						Statement st = con.createStatement();
						String query = "insert into designations (designation) values('" + tf.getValue().toString().trim()
								+ "')";
						st.executeUpdate(query);
						st.close();
						con.close();
						 
						Notification.show("Successfully Designation Added", Type.WARNING_MESSAGE);
						  
						EmpApprovalSub empApprovalSub = new EmpApprovalSub();						
						empApprovalSub.designation_load();
						
						close(); // Close the sub-window
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}

				

			}
		});
		

	}

	 
	
//	void designation_load() {
//		try {
//			Connection co = JDBC_MY.con();
//			Statement st = co.createStatement();
//			ResultSet re = st.executeQuery("SELECT * FROM designations");
//			while (re.next()) {
//				comboBox_designation.addItem(re.getString("designation"));
//			}
//			re.close();
//			st.close();
//			co.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//
//	}

}
